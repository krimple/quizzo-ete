package org.springframework.samples.async.quizzo.controller;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.Player;
import org.springframework.data.examples.quizzo.domain.PlayerAnswer;
import org.springframework.data.examples.quizzo.repository.PlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.samples.async.config.AppConfig;
import org.springframework.samples.async.config.WebMvcConfig;
import org.springframework.samples.async.quizzo.controller.gamestatus.GameComplete;
import org.springframework.samples.async.quizzo.controller.gamestatus.GameStatus;
import org.springframework.samples.async.quizzo.controller.gamestatus.WaitingForNextQuestion;
import org.springframework.samples.async.quizzo.controller.moderator.GameStartedResponse;
import org.springframework.samples.async.quizzo.controller.moderator.command.*;
import org.springframework.samples.async.quizzo.controller.moderator.ModeratorCommands;
import org.springframework.samples.async.quizzo.engine.GameRunEngine;
import org.springframework.samples.async.quizzo.responses.GameJoinedResponse;
import org.springframework.samples.async.quizzo.responses.QuestionPendingResponse;
import org.springframework.samples.async.quizzo.responses.QuizPollResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.UnsupportedEncodingException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * A crude end-to-end test suite with a beheadded UI layer...
 * <p/>
 * This shows a full use case of the administrator controlling the game, while the end users play the game
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, WebMvcConfig.class})
public class QuizContainerTest {

    @Autowired
    GamePlayController gamePlayController;

    @Autowired
    GameStatusController gameStatusController;

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
    public void testStartQuizJoinGameAndAskForQuestion() throws UnsupportedEncodingException {
        String gameId = null;

        // destroy user if already there
        if (playerRepository.findOneByName("foobar") != null) {
            playerRepository.delete("foobar");
        }

        try {
            // initialize the game
            StartGame startGameCommand = new StartGame();
            startGameCommand.setGameTitle("JavascriptQuiz To Play");
            startGameCommand.setQuizId("JavascriptQuiz");
            // if this fails, we didn't get a successful return type - may need refactoring
            GameStartedResponse startedResponse = (GameStartedResponse) quizModeratorController.startQuiz(startGameCommand);

            assertTrue(startedResponse.getCategory().equals("GameStarted"));
            gameId = startedResponse.getGameId();
            assertNotNull(gameId);

            // user-land - create a player
            ResponseEntity<Player> playerResponse =
                    playerController.registerUserByNickName(mockSession, "foobar");
            assertNotNull(playerResponse);

            // user-land - join the game...
            QuizPollResponse joinGameResponse = gamePlayController.joinGame(mockSession, gameId);
            assertTrue(joinGameResponse.getClass().isAssignableFrom(GameJoinedResponse.class));

            // start the game play!
            BeginPlay playGameCommand = new BeginPlay();
            playGameCommand.setGameId(gameId);
            quizModeratorController.moderate(playGameCommand, mockSession, mockResponse);

            while (true) {
                GameStatus pollStatus = (GameStatus) gameStatusController.getGameStatus(mockSession);
                if (pollStatus.getStatus().equals("GameComplete")) break;

                QuestionPendingResponse questionPendingResponse =
                        (QuestionPendingResponse) gamePlayController.getCurrentQuestion(mockSession, mockResponse);
                MultipleChoiceQuestion question = questionPendingResponse.getQuestion();
                // always safe choosing the first one ;)
                PlayerAnswer answer = new PlayerAnswer(question.getQuestionNumber(), 'a');

                gamePlayController.submitPlayerAnswer(answer, mockSession);

                // moderator - end question play
                EndQuestion endQuestionCommand = new EndQuestion();
                endQuestionCommand.setGameId(gameId);
                quizModeratorController.moderate(endQuestionCommand, mockSession, mockResponse);

                // user-land - poll for next step and make sure we're awaiting the next score OR done
                pollStatus = (GameStatus) gameStatusController.getGameStatus(mockSession);
                if (pollStatus.getClass().equals(WaitingForNextQuestion.class)) {
                    NextQuestion nextQuestionCommand = new NextQuestion();
                    nextQuestionCommand.setGameId(gameId);
                    quizModeratorController.moderate(nextQuestionCommand, mockSession, mockResponse);
                } else if (pollStatus.getClass().equals(GameComplete.class)) {
                    break;  // get me outta here!
                }
            }

            DestroyGame destroyGameCommand = new DestroyGame();
            destroyGameCommand.setGameId(gameId);

            quizModeratorController.moderate(destroyGameCommand, mockSession, mockResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            // avoid existing user error next time
            playerRepository.delete("foobar");
        }
    }

    @After
    public void tearDown() {

    }
}
