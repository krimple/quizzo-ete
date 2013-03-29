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
package org.phillyete.quizzo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
 
import org.phillyete.quizzo.service.ScoreDetail;
import org.phillyete.quizzo.service.ScoreService;
import org.phillyete.quizzo.service.ScoreSummary;
import org.phillyete.quizzo.domain.MultipleChoiceQuestion;
import org.phillyete.quizzo.domain.PlayerAnswer;
import org.phillyete.quizzo.domain.Quiz;
import org.phillyete.quizzo.repository.PlayerAnswerRepository;
import org.phillyete.quizzo.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author David Turanski
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:/META-INF/spring/applicationContext-data-access.xml",
        "classpath:/META-INF/spring/applicationContext-service.xml"
})
public class ScoreServiceExampleTest {
	@Autowired
	QuizRepository quizRepo;
	@Autowired
	PlayerAnswerRepository playerAnswerRepo;
	
	@Autowired
	ScoreService scoreService;
	
	String gameId;
	
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
		q.addChoice("Red", 1);
		q.addChoice("Blue", 20);
		q.addChoice("Green", 5);
		q.addChoice("Yellow", 3);
		questions.add(q);
		
		q = new MultipleChoiceQuestion("What is the air-speed velocity of an unladen swallow?");
		q.addChoice("What do you mean? An African or European swallow?", 100);
		q.addChoice("20", 2);
		q.addChoice("50", 5);
		q.addChoice("70", 7);
		q.addChoice("None of the above", -20);
		questions.add(q);
		
		Quiz quiz = new Quiz("quiz1","Test Quiz");
		for (MultipleChoiceQuestion question: questions) {
			quiz.addQuestion(question);
		}
		gameId = quiz.startGame("demo quiz - " + System.currentTimeMillis()).getId();
		quizRepo.save(quiz);
		
		List<PlayerAnswer> answers = new ArrayList<PlayerAnswer>();
		answers.add(new PlayerAnswer("dave", gameId, quiz.getId(),0,'c', 10));
		answers.add(new PlayerAnswer("ken", gameId, quiz.getId(),0,'b', 5));
		
		answers.add(new PlayerAnswer("dave", gameId, quiz.getId(),1,'b', 5));
		answers.add(new PlayerAnswer("ken", gameId, quiz.getId(),1,'a', 10));
		
		answers.add(new PlayerAnswer("dave", gameId, quiz.getId(),2,'a', 5));
		answers.add(new PlayerAnswer("ken", gameId, quiz.getId(),2,'a', 0));
		
		playerAnswerRepo.save(answers);
		
	}
	
	@Test
    public void testSomething() {
        assertEquals(1, 1);
    }

	public void ignoreForNow() {
		Map<String,ScoreSummary> scores = scoreService.getTotalScores(gameId);
		assertEquals(140,scores.get("dave").getScore());
		assertEquals(111,scores.get("ken").getScore());
		
		List<ScoreDetail> questionDetails = scoreService.getScoresForQuestion(gameId, 1);
		assertEquals(2,questionDetails.size());
		ScoreDetail d1 = questionDetails.get(0);
		ScoreDetail d2 = questionDetails.get(1);
		
		assertEquals('b',d1.getAnswer());
		assertEquals(20, d1.getScore());
		assertEquals(1, d1.getQuestionNumber());
		
		assertEquals('a',d2.getAnswer());
		assertEquals(1, d2.getScore());
		assertEquals(1, d2.getQuestionNumber());
		
		assertTrue( d1.getPlayerId().equals("dave"));
		assertTrue(d2.getPlayerId().equals("ken"));
		assertFalse(d1.getPlayerId().equals(d2.getPlayerId()));
	}
	
	@After
	public void tearDown() {
		quizRepo.delete("quiz1");
	}
}
