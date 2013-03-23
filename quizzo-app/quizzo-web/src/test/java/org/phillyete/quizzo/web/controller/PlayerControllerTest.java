package org.phillyete.quizzo.web.controller;

import static junit.framework.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.phillyete.quizzo.domain.Player;
import org.phillyete.quizzo.repository.PlayerRepository;
import org.phillyete.quizzo.web.config.WebMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//for the MVC test DSL

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class, AppConfig.class })
public class PlayerControllerTest {

    @Autowired
    private PlayerController playerController;

    @Autowired
    private PlayerRepository playerRepository;

    private Player myPlayer;

    @Before
    public void setUp() {
        myPlayer = new Player("28i30ur3qkwlejf");
        playerRepository.save(myPlayer);
    }

    @After
    public void tearDown() {
        playerRepository.delete(myPlayer);
    }

    @Test
    public void testContext() {
        assertNotNull(playerController);
    }

    @Test
    public void addPlayer() {
        MockHttpSession session = new MockHttpSession();
        try {
            ResponseEntity<Player> player =
                    playerController.registerUserByNickName(session, "foobar2342352352");
            assertNotNull(player);
            assertEquals(HttpStatus.CREATED, player.getStatusCode());
            assertEquals("foobar2342352352", player.getBody().getName());
        } finally {
        // clean up after ourselves in this test
            playerRepository.delete("foobar2342352352");
        }
    }

    @Test
    public void addBadPlayerReturnsNoContent() {
        MockHttpSession session = new MockHttpSession();
        // already exists
        ResponseEntity<Player> player =
                playerController.registerUserByNickName(session, "28i30ur3qkwlejf");
        assertNotNull(player);
        assertEquals(HttpStatus.NO_CONTENT, player.getStatusCode());
    }

}
