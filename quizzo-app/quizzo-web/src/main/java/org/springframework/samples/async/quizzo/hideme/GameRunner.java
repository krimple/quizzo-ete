/*
 * Copyright 2002-2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.samples.async.quizzo.hideme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.Choice;
import org.springframework.data.examples.quizzo.domain.Game;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.PlayerAnswer;
import org.springframework.data.examples.quizzo.domain.Quiz;
import org.springframework.data.examples.quizzo.repository.PlayerAnswerRepository;
import org.springframework.data.examples.quizzo.repository.PlayerRepository;
import org.springframework.data.examples.quizzo.repository.QuizRepository;
import org.springframework.samples.async.quizzo.hideme.QuestionFeeder;
import org.springframework.util.Assert;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author David Turanski
 *
 */
/**
 * This runs in a background thread, waiting QUESTION_TIME_INTERVAL ms 
 * for the next question and setting deferred results when it arrives
 * @author David Turanski
 *
 * TODO - figure out how to cancel a game - cancel request atomic?
 */
public class GameRunner implements Runnable {
	private static Log logger = LogFactory.getLog(GameRunner.class);
	private final List<DeferredResult<MultipleChoiceQuestion>> requests = Collections
			.synchronizedList(new ArrayList<DeferredResult<MultipleChoiceQuestion>>());
	private Quiz quiz;
    // TODO - Maybe we implement an enum for the lifecycle here - AWAITING_PLAYERS, STARTED, FINISHED?
    // For the nextq request we had to also determine whether the game was started in the first place
    public AtomicBoolean gameStarted = new AtomicBoolean(false);
	public AtomicBoolean gameInProgress = new AtomicBoolean();
	private long questionExpiryTime;
	private Game game;
	private final QuizRepository quizRepository;
	private final PlayerAnswerRepository playerAnswerRepository;
	private MultipleChoiceQuestion currentQuestion;
	private PlayerRepository playerRepository;
	
	public static enum AnswerStatus {
		NO_GAME_INPROGRESS, 
		INVALID_QUESTION_NUMBER,
		INVALID_CHOICE,
		QUESTION_EXPIRED,
		PLAYER_NOT_REGISTERED,
		DUPLICATE_ANSWER,
		ANSWER_SUBMITTED}

	/**
	 * @param quizRepository
	 */
	@Autowired
	public GameRunner(QuizRepository quizRepository, PlayerAnswerRepository playerAnswerRepository, PlayerRepository playerRepository) {
		this.quizRepository = quizRepository;
		this.playerAnswerRepository = playerAnswerRepository;
		this.playerRepository = playerRepository;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Assert.notNull(quiz, "cannot start with a null quiz!");
		logger.debug("running game ...");

		//	Start yet another thread to serve questions over a BlockingQueue 
		//	at the configured interval relative to the start of the game this is necessary because 
		//	the timing of the questions is asynchronous relative to the arrival of requests
		CountDownLatch latch = new CountDownLatch(1);
		QuestionFeeder questionFeeder = new QuestionFeeder(quiz,latch);
		questionFeeder.setQuestionExpiryTime(questionExpiryTime);
		
		new Thread(questionFeeder).start();
		try {
			latch.await(1000,TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException("Error encountered starting game", e);
		}
        gameStarted.set(true);
		gameInProgress.set(true);
		logger.debug("game in progress...");
		while (gameInProgress.get()) {
			currentQuestion = questionFeeder.getNextQuestion();
			gameInProgress.set(currentQuestion != null);
			for (DeferredResult<MultipleChoiceQuestion> request : requests) {
				logger.debug("calling setResult on request..." + request);
				request.setResult(currentQuestion);
			}
			
		}
		logger.debug("game over");
		this.quiz = null;
	}

	void addRequest(DeferredResult<MultipleChoiceQuestion> request) {
		logger.debug("adding request ..." + request);
		this.requests.add(request);
	}

	void removeRequest(DeferredResult<MultipleChoiceQuestion> request) {
		logger.debug("removing request ..." + request);
		this.requests.remove(request);
	}

	synchronized Game startGame(Quiz quiz) {
		this.quiz = quiz;
		game = quiz.startGame("automatically generated game - @" + System.currentTimeMillis());
		quizRepository.save(quiz);
		//Start a new game
		new Thread(this).start();
		return game;
	}

	boolean isGameInProgress() {
		return gameInProgress.get();
	}

	void setQuestionExpiryTime(long questionExpiryTime) {
		this.questionExpiryTime = questionExpiryTime;
	}
	
	synchronized AnswerStatus submitPlayerAnswer(PlayerAnswer answer) {
		Assert.notNull(answer, "answer cannot be null");
		Assert.notNull(answer.getPlayerId(), "answer contains a null player ID");
		if (playerRepository.findOne(answer.getPlayerId()) == null){
			return AnswerStatus.PLAYER_NOT_REGISTERED;
		}
		if (!isGameInProgress()) {
			return AnswerStatus.NO_GAME_INPROGRESS;
		}
		if (answer.getQuestionNumber() < currentQuestion.getQuestionNumber()) {
			return AnswerStatus.QUESTION_EXPIRED;
		}
		if (answer.getQuestionNumber() > currentQuestion.getQuestionNumber()) {
			return AnswerStatus.INVALID_QUESTION_NUMBER;
		}
		
		if (!currentQuestion.getChoices().keySet().contains(
				Choice.Letter.fromChar(answer.getChoice()))) {
			return AnswerStatus.INVALID_CHOICE;
		}
		
		answer.setQuizId(quiz.getId());
		answer.setGameId(game.getId());
		if (playerAnswerRepository.findByGameIdAndPlayerIdAndQuestionNumber(
				answer.getGameId(), answer.getPlayerId(), answer.getQuestionNumber()) != null) {
			return AnswerStatus.DUPLICATE_ANSWER;
		}
		
		playerAnswerRepository.save(answer);
		return AnswerStatus.ANSWER_SUBMITTED;
	}
}
