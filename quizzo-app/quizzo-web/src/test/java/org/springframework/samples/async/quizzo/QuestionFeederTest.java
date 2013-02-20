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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.Quiz;

/**
 * @author David Turanski
 *
 */

public class QuestionFeederTest {
	Quiz quiz;
	@Before
	public void setUp() {
		List<MultipleChoiceQuestion> questions = new ArrayList<MultipleChoiceQuestion>();
		MultipleChoiceQuestion q = new MultipleChoiceQuestion("What is the best programming language");
		q.addChoice("Java", 5);
		q.addChoice("Javascript", 10);
		q.addChoice("Groovy", 20);
		q.addChoice("Cobol", 0);
		
		questions.add(q);
		
		q = new MultipleChoiceQuestion("What is your favorite color?");
		q.addChoice("Red", 0);
		q.addChoice("Blue", 0);
		q.addChoice("Green", 0);
		q.addChoice("Yellow", 0);
		questions.add(q);
		
		quiz = new Quiz("quiz1","Test Quiz");
		for (MultipleChoiceQuestion question: questions) {
			quiz.addQuestion(question);
		}
	}

	@Test
	public void test() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		QuestionFeeder quizzo = new QuestionFeeder(quiz, latch);
		quizzo.setQuestionExpiryTime(500);
		new Thread(quizzo).start();
		latch.await(500,TimeUnit.MILLISECONDS);
		assertEquals(quiz.getQuestions().get(0),quizzo.getNextQuestion());
		assertEquals(quiz.getQuestions().get(1),quizzo.getNextQuestion());
		assertNull(quizzo.getNextQuestion());
	}
}
