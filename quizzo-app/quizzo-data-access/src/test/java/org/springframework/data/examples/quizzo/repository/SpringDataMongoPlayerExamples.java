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

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.config.DataAccessConfig;
import org.springframework.data.examples.quizzo.domain.Player;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author David Turanski
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DataAccessConfig.class})
public class SpringDataMongoPlayerExamples {
	@Autowired
	public PlayerRepository playerRepo;
	Player dave = new Player("dave000000");
	Player ken = new Player("ken000000");
	@Before
	public void setUp() {
		
		playerRepo.save(dave);
		playerRepo.save(ken);
	}
	@Test
	public void test() {
		assertEquals(2,playerRepo.count());
		Player p1 = playerRepo.findOne(dave.getName());
		assertEquals(dave.getName(),p1.getName());
		
	}
	
	@After
	public void tearDown() {
		playerRepo.delete(dave);
		playerRepo.delete(ken);
	}
}
