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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.phillyete.quizzo.domain.Player;
import org.phillyete.quizzo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author David Turanski, Ken Rimple
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext-data-access.xml" })
public class SpringDataMongoPlayerExamplesTest {

	@Autowired
	public PlayerRepository playerRepo;

	Player dave = new Player("dave01");
	Player ken = new Player("ken01");
    Player ken2 = new Player("ken02");

    @Before
	public void setUp() {
		playerRepo.save(dave);
		playerRepo.save(ken);
        playerRepo.save(ken2);
	}

    @Test
    public void testFindOne() {
        Player p1 = playerRepo.findOne(dave.getName());
		assertEquals(dave.getName(),p1.getName());
	}

    @Test
    public void testFindByNameLikeMultiMatch() {
        List<Player> players = playerRepo.findByNameLike("ken");
        assertEquals(2, players.size());
    }

    @Test
    public void testFindByNameLikeSingleMatch() {
        List<Player> players = playerRepo.findByNameLike("ken02");
        assertEquals(1, players.size());
    }

    @Test
    public void testFindOneByName() {
        Player player = playerRepo.findOneByName("dave01");
        assertNotNull(player);
        assertEquals("dave01", player.getName());
    }

	@After
	public void tearDown() {
		playerRepo.delete(dave);
		playerRepo.delete(ken);
        playerRepo.delete(ken2);
	}
}
