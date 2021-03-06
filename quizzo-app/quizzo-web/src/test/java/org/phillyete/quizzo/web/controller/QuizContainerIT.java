package org.phillyete.quizzo.web.controller;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.phillyete.quizzo.web.controller.gamestatus.GameComplete;
import org.phillyete.quizzo.web.controller.gamestatus.GameStatus;
import org.phillyete.quizzo.web.controller.gamestatus.WaitingForNextQuestion;
import org.phillyete.quizzo.web.controller.moderator.GameStartedResponse;
import org.phillyete.quizzo.domain.MultipleChoiceQuestion;
import org.phillyete.quizzo.domain.Player;
import org.phillyete.quizzo.domain.PlayerAnswer;
import org.phillyete.quizzo.web.engine.GameRunEngine;
import org.phillyete.quizzo.repository.PlayerRepository;
import org.phillyete.quizzo.responses.GameJoinedResponse;
import org.phillyete.quizzo.responses.QuestionPendingResponse;
import org.phillyete.quizzo.responses.QuizPollResponse;
import org.phillyete.quizzo.web.config.WebMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.UnsupportedEncodingException;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * A crude end-to-end test suite with a beheadded UI layer...
 * <p/>
 * This shows a full use case of the administrator controlling the game, while the end users play the game
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = {
        "classpath*:/META-INF/spring/applicationContext*.xml",
        "classpath:/META-INF/spring-web/applicationContext-webmvc.xml"
})
public class QuizContainerIT {

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
            String quizId = "JavascriptQuiz";
            String title = "Play the Javascript Quiz!";

                // if this fails, we didn't get a successful return type - may need refactoring
                GameStartedResponse startedResponse =
                        (GameStartedResponse) quizModeratorController.startQuiz(quizId, title);

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
            quizModeratorController.beginPlay(gameId);

            while (true) {
                GameStatus pollStatus = (GameStatus) gameStatusController.getGameStatus(mockSession);
                if (pollStatus.getStatus().equals("GameComplete")) break;

                QuestionPendingResponse questionPendingResponse =
                        (QuestionPendingResponse) gamePlayController.getCurrentQuestion(mockSession, mockResponse);
                MultipleChoiceQuestion question = questionPendingResponse.getQuestion();
                // always safe choosing the first one ;)
                PlayerAnswer answer = new PlayerAnswer(question.getQuestionNumber(), "a");

                gamePlayController.submitPlayerAnswer(answer, mockSession);

                // moderator - end question play
                quizModeratorController.endQuestion(gameId);

                // user-land - poll for next step and make sure we're awaiting the next score OR done
                pollStatus = (GameStatus) gameStatusController.getGameStatus(mockSession);
                if (pollStatus.getClass().equals(WaitingForNextQuestion.class)) {
                    quizModeratorController.nextQuestion(gameId);
                } else if (pollStatus.getClass().equals(GameComplete.class)) {
                    break;  // get me outta here!
                }
            }
            quizModeratorController.destroyGame(gameId);
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
