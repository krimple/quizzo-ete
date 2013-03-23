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

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.phillyete.quizzo.domain.PlayerAnswer;
import org.phillyete.quizzo.repository.PlayerAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author David Turanski
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext-data-access.xml" })
public class SpringDataMongoPlayerAnswerExamplesTest {

	@Autowired
	public PlayerAnswerRepository playerAnswerRepo;

	@Before
	public void setUp() {
		PlayerAnswer ans = new PlayerAnswer("p1", "i1", "q1", 0, 'a', 100);
		playerAnswerRepo.save(ans);
        ans = new PlayerAnswer("p1", "i1", "q1", 1,  'a', 100);
        playerAnswerRepo.save(ans);
		ans = new PlayerAnswer("p2", "i1", "q1", 0, 'b', 100);
		playerAnswerRepo.save(ans);
	}

	@Test
	public void testCreateAndSumUp() {
		List<PlayerAnswer> answers = playerAnswerRepo.findByQuizId("q1");
		assertEquals(3, answers.size());

		answers = playerAnswerRepo.findByGameId("i1");
		assertEquals(3, answers.size());

		answers = playerAnswerRepo.findByGameIdAndPlayerId("i1", "p1");
		assertEquals(2, answers.size());

		answers = playerAnswerRepo.findByGameIdAndQuestionNumber("i1", 0);
		assertEquals(2, answers.size());
		
		assertNotNull(playerAnswerRepo.findByGameIdAndPlayerIdAndQuestionNumber("i1","p1", 0));
		// assertNull(playerAnswerRepo.findByGameIdAndPlayerIdAndQuestionNumber("i1","p1",1));
		assertNull(playerAnswerRepo.findByGameIdAndPlayerIdAndQuestionNumber("i2","p1",0));
		assertNull(playerAnswerRepo.findByGameIdAndPlayerIdAndQuestionNumber("i1","p3",0));

        int score = playerAnswerRepo.calculateScore("i1", "p1");
        assertEquals(200, score);

	}

	@After
	public void tearDown() {
		playerAnswerRepo.delete(playerAnswerRepo.findByQuizId("q1"));
	}
}
