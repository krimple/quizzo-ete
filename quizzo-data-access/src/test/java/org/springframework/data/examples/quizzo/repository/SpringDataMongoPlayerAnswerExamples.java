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
package org.springframework.data.examples.quizzo.repository;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.config.ApplicationConfig;
import org.springframework.data.examples.quizzo.domain.Choice;
import org.springframework.data.examples.quizzo.domain.Choice.Letter;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.PlayerAnswer;
import org.springframework.data.examples.quizzo.domain.Quiz;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author David Turanski
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ApplicationConfig.class})
public class SpringDataMongoPlayerAnswerExamples {
	@Autowired
	public PlayerAnswerRepository playerAnswerRepo;
	
	@Before
	public void setUp() {
	
	}
	@Test
	public void test() {
		//PlayerAnswer ans = new PlayerAnswer("p1", "i1", "q1", 0, 'a');
		//playerAnswerRepo.save(ans);
		//ans = new PlayerAnswer("p2", "i1", "q1", 0, 'b');
		//playerAnswerRepo.save(ans);
		List<PlayerAnswer> answers = playerAnswerRepo.findAll();
		assertEquals(2,answers.size());
	}
	
	@After
	public void tearDown() {
		//playerAnswerRepo.deleteAll();
	}
}
