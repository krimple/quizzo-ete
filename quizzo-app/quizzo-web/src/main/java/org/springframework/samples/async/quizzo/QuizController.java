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
package org.springframework.samples.async.quizzo;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.Game;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.PlayerAnswer;
import org.springframework.data.examples.quizzo.domain.Quiz;
import org.springframework.data.examples.quizzo.repository.QuizRepository;
import org.springframework.samples.async.quizzo.GameRunner.AnswerStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Contoller to run Quizzo game and access related resources
 *
 * @author David Turanski
 *
 */
@Controller
@RequestMapping("/quiz")
public class QuizController {
	private static Log logger = LogFactory.getLog(QuizController.class);
	//TODO - This should be a property
	private static final long QUESTION_EXPIRY_TIME = 15000;

	private final QuizRepository quizRepository;
	private final GameRunner gameRunner;

	@Autowired
	public QuizController(QuizRepository quizRepository, GameRunner gameRunner) {
		this.quizRepository = quizRepository;
		this.gameRunner = gameRunner;
		this.gameRunner.setQuestionExpiryTime(QUESTION_EXPIRY_TIME);
	}

	/**
	 * Start a new game. This should be called by the game administration app
	 * @param quizId
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "start/{quizId}", method = RequestMethod.POST)
	@ResponseBody
	public Game startGame(@PathVariable String quizId, HttpServletResponse response) {

		Quiz quiz = getQuizById(quizId);
		if (quiz == null) {
			sendHttpStatusResponse(404, "quiz " + quizId + " not found.", response);
		}
		return gameRunner.startGame(quiz);
	}

	@RequestMapping(value = "answer", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void submitAnswer(@RequestBody PlayerAnswer answer, HttpServletResponse response) {
		logger.debug("submit answer from " + answer.getPlayerId() + " question:" + answer.getQuestionNumber()
				+ " choice: " + answer.getChoice());
		AnswerStatus status = gameRunner.submitPlayerAnswer(answer);
		switch (status) {
		case ANSWER_SUBMITTED:
			sendHttpStatusResponse(201, "answer submitted", response);
			break;
		case NO_GAME_INPROGRESS:
			sendHttpStatusResponse(403, "no game in progress", response);
			break;
		case QUESTION_EXPIRED:
			sendHttpStatusResponse(403, "the question answered is no longer active", response);
			break;

		case INVALID_QUESTION_NUMBER:
			sendHttpStatusResponse(403, "question number provided does not match the currently active question",
					response);
			break;
		case PLAYER_NOT_REGISTERED:
			sendHttpStatusResponse(403, "player " + answer.getPlayerId()+" is not registered",
					response);
			break;
		case INVALID_CHOICE:
			sendHttpStatusResponse(403, "'" + answer.getChoice()+"' is not a valid choice for this question",
					response);
			break;
		case DUPLICATE_ANSWER:
			sendHttpStatusResponse(403,"player " + answer.getPlayerId()+" has already answered this question",
					response);
			break;
		}
	}

	/**
	 * Serve the next question asynchronously
	 * @param response the HttpServletResponse used for error handling
	 * @return  wait for the configured time interval
	 * before responding unless this is the first question.
	 * 
	 * This should be called when the page is loaded and after each response is submitted. 
	 * If the game hasn't started the response thread will stay open until the game starts, if a game is 
	 * in progress, it will return the next question 
	 */
	@RequestMapping(value = "nextq", method = RequestMethod.GET)
	@ResponseBody
	public DeferredResult<MultipleChoiceQuestion> getNextQuestion(HttpServletResponse response) {
		logger.debug("got nextq request");
		//TODO: Anything special we need to indicate the game is over? - Currently nextq returns null after last question
		//		if (questionRequestProcessor.isGameOver()) {
		//			logger.debug("game over");
		//			sendHttpStatusResponse(204, "Game over - no more questions", response);
		//			return null;
		//		}

		final DeferredResult<MultipleChoiceQuestion> deferredResult = new DeferredResult<MultipleChoiceQuestion>();
		deferredResult.onCompletion(new Runnable() {
			@Override
			public void run() {
				gameRunner.removeRequest(deferredResult);
			}
		});

		gameRunner.addRequest(deferredResult);
		return deferredResult;
	}

	/**
	 * This is a synchronous query to retrieve a specific question, more for administrative purposes not used 
	 * during a live game
	 * @param quizId
	 * @param questionIndex
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "question/{quizId}/{questionIndex}", method = RequestMethod.GET)
	@ResponseBody
	public MultipleChoiceQuestion getQuestion(@PathVariable String quizId, @PathVariable int questionIndex,
			HttpServletResponse response) {
		logger.debug("got question request");
		Quiz quiz = quizRepository.findOne(quizId);
		;
		if (quiz == null) {
			sendHttpStatusResponse(404, "quiz " + quizId + " not found", response);
		}
		MultipleChoiceQuestion question = null;
		try {
			question = quiz.getQuestions().get(questionIndex);
		} catch (Exception e) {
			sendHttpStatusResponse(404, "invalid question number " + questionIndex, response);
		}
		return question;
	}

	private synchronized Quiz getQuizById(String quizId) {
		return quizRepository.findOne(quizId);
	}

	private void sendHttpStatusResponse(int statusCode, String message, HttpServletResponse response) {
		try {
			response.sendError(statusCode, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
