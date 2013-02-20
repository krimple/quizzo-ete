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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.Quiz;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * @author David Turanski
 *
 */
class QuestionFeeder implements Runnable {
	private static Log logger = LogFactory.getLog(QuestionFeeder.class);
	private long questionExpiryTime;
	private final Quiz quiz;
	private final BlockingQueue<MultipleChoiceQuestion> questions;
	private final CountDownLatch latch;

	QuestionFeeder(Quiz quiz, CountDownLatch latch) {
		this.latch = latch;
		this.quiz = quiz;
		questions = new LinkedBlockingQueue<MultipleChoiceQuestion>();
	}

	void setQuestionExpiryTime(long questionExpiryTime) {
		this.questionExpiryTime = questionExpiryTime;
	}
	
	MultipleChoiceQuestion getNextQuestion() {
		MultipleChoiceQuestion question = null;
		try {
			question =  questions.poll(questionExpiryTime*1100, TimeUnit.MICROSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.debug("getNextQuestion returning question " + (question == null? "null":question.getQuestionNumber()));
		return question;
	}
	

	synchronized void reset() {
		questions.clear();
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		logger.debug("running quiz...");
		questions.clear();
		Assert.isTrue(!CollectionUtils.isEmpty(quiz.getQuestions()),"the quiz " + quiz.getId() + " contains no questions");
		for (MultipleChoiceQuestion question : quiz.getQuestions()) {
			try {
				questions.put(question);
				latch.countDown();
				Thread.sleep(questionExpiryTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
	}
}

