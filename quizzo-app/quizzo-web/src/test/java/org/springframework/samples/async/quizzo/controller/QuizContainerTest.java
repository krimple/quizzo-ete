package org.springframework.samples.async.quizzo.controller;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.Player;
import org.springframework.data.examples.quizzo.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.samples.async.config.AppConfig;
import org.springframework.samples.async.config.WebMvcConfig;
import org.springframework.samples.async.quizzo.engine.GameRunEngine;
import org.springframework.samples.async.quizzo.responses.GameJoinedResponse;
import org.springframework.samples.async.quizzo.responses.QuizPollResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebMvcConfig.class })
public class QuizContainerTest {

    @Autowired
    GamePlayController gamePlayController;

    @Autowired
    QuizModeratorController quizModeratorController;

    @Autowired
    PlayerController playerController;

    @Autowired
    GameRunEngine gameRunEngine;

    @Autowired
    PlayerRepository playerRepository;

    MockHttpSession mockSession;

    MockHttpServletResponse mockResponse;


    @Before
    public void setUp() {
        mockSession = new MockHttpSession();
        mockResponse = new MockHttpServletResponse();
    }

    @Test
    @DirtiesContext
    public void testStartQuizJoinGameAndAskForQuestion() {

        // destroy user if already there
        if (playerRepository.findOneByName("foobar") !=  null) {
            playerRepository.delete("foobar");
        }

        try {
            // begin the game
            quizModeratorController.startGame("JavascriptQuiz", "silly", "jstest", mockSession, mockResponse);
            assertEquals(200, mockResponse.getStatus());

            // create and register our user in the game
            ResponseEntity<Player> playerResponse =
                    playerController.registerUserByNickName(mockSession, "foobar");
            assertNotNull(playerResponse);

            QuizPollResponse joinGameResponse = gamePlayController.joinGame(mockSession, "jstest");
            assertTrue(joinGameResponse.getClass().isAssignableFrom(GameJoinedResponse.class));

            // start the game play!
            quizModeratorController.beginPlay(mockSession);

            // check for the first question!
            QuizPollResponse response = gamePlayController.getCurrentQuestion(mockSession, mockResponse);

            Assert.assertTrue(response.getCategory().equals("QuestionPending"));

        } finally {
            // avoid existing user error
            playerRepository.delete("foobar");
        }
    }

    @After
    public void tearDown() {

    }
}
