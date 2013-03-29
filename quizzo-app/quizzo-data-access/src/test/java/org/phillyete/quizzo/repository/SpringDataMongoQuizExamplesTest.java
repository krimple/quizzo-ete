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
package org.phillyete.quizzo.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.phillyete.quizzo.domain.Choice;
import org.phillyete.quizzo.domain.MultipleChoiceQuestion;
import org.phillyete.quizzo.domain.Quiz;
import org.phillyete.quizzo.domain.Choice.Letter;
import org.phillyete.quizzo.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author David Turanski
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext-data-access.xml" })
public class SpringDataMongoQuizExamplesTest {
	@Autowired
	public QuizRepository quizRepo;
	
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
		
		Quiz quiz = new Quiz("quiz1","Test Quiz");
		for (MultipleChoiceQuestion question: questions) {
			quiz.addQuestion(question);
		}
		quiz.startGame("test - " + quiz.getTitle() + " - @" + System.currentTimeMillis());
        quiz.startGame("second test - " + quiz.getTitle() + " - @" + System.currentTimeMillis());

        quizRepo.save(quiz);
	}
	@Test
	public void test() {
		Quiz quiz = quizRepo.findOne("quiz1");
		assertNotNull(quiz);
		assertEquals(2,quiz.getGamesPlayed().size());
		
		List<MultipleChoiceQuestion> questions = quiz.getQuestions();
		assertEquals(2,questions.size());
		MultipleChoiceQuestion question = questions.get(0);
		Choice choice = question.getChoice('a');
		assertEquals("Java",choice.getText());
		assertEquals('a',choice.getLetter());
		assertEquals(5, choice.getScore());
		
		Map<Letter,Choice> choices = question.getChoices();
		assertNotNull(choices.get(Letter.a));
		assertNotNull(choices.get(Letter.b));
		assertNotNull(choices.get(Letter.c));
		assertNotNull(choices.get(Letter.d));
		assertNull(choices.get(Letter.e));
	
		question = questions.get(1);
		assertEquals("What is your favorite color?",question.getText());
	}
	
	@After
	public void tearDown() {
		quizRepo.delete("quiz1");
	}
}
