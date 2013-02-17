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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.Game;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.Quiz;
import org.springframework.data.examples.quizzo.repository.QuizRepository;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
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
	private static final long QUESTION_TIME_INTERVAL = 15000;

	private final QuizRepository quizRepository;
	private final QuestionRequestProcessor questionRequestProcessor;

	@Autowired
	public QuizController(QuizRepository quizRepository) {
		this.quizRepository = quizRepository;
		this.questionRequestProcessor = new QuestionRequestProcessor();
	}

	/**
	 * Start a new game. This should be called by the game administration app
	 * @param quizId
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "start/{quizId}", method = RequestMethod.GET)
	@ResponseBody
	public Game startGame(@PathVariable String quizId, HttpServletResponse response) {

		Quiz quiz = getQuizById(quizId);
		if (quiz == null) {
			sendHttpStatusResponse(404, "quiz " + quizId + " not found.", response);
		}
		return newGame(quiz);
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
				questionRequestProcessor.removeRequest(deferredResult);
			}
		});

		questionRequestProcessor.addRequest(deferredResult);
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

	private synchronized Game newGame(Quiz quiz) {
		//Record the game
		Game game = quiz.startGame();
		quizRepository.save(quiz);

		//Start feeding questions
		questionRequestProcessor.setQuiz(quiz);
		new Thread(questionRequestProcessor).start();
		return game;
	}

	/**
	 * This runs in a background thread, waiting QUESTION_TIME_INTERVAL ms 
	 * for the next question and setting deferred results when it arrives
	 * @author David Turanski
	 *
	 */
	static class QuestionRequestProcessor implements Runnable {
		private static Log logger = LogFactory.getLog(QuizController.class);
		private final List<DeferredResult<MultipleChoiceQuestion>> requests = Collections
				.synchronizedList(new ArrayList<DeferredResult<MultipleChoiceQuestion>>());
		private Quiz quiz;

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			logger.debug("running question processor ...");
			Assert.notNull(quiz, "cannot start with a null quiz!");

			//	Start yet another thread to serve questions over a BlockingQueue 
			//	at the configured interval relative to the start of the game this is necessary because 
			//	the timing of the questions is asynchronous relative to the arrival of requests
			Quizzo quizzo = new Quizzo(quiz);
			quizzo.setQuestionExpiryTime(QUESTION_TIME_INTERVAL);
			new Thread(quizzo).start();

			boolean done = false;
			while (!done) {
				MultipleChoiceQuestion question = quizzo.getNextQuestion();
				for (DeferredResult<MultipleChoiceQuestion> request : requests) {
					logger.debug("calling setResult on request..." + request);
					request.setResult(question);
				}
				done = question == null;
			}
			this.quiz = null;
		}

		public void addRequest(DeferredResult<MultipleChoiceQuestion> request) {
			logger.debug("adding request ..." + request);
			this.requests.add(request);
		}

		public void removeRequest(DeferredResult<MultipleChoiceQuestion> request) {
			logger.debug("removing request ..." + request);
			this.requests.remove(request);
		}

		public void setQuiz(Quiz quiz) {
			this.quiz = quiz;
		}
	}

}
