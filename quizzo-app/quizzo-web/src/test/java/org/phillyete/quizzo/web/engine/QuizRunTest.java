package org.phillyete.quizzo.web.engine;

import org.junit.Before;
import org.junit.Test;
import org.phillyete.quizzo.domain.MultipleChoiceQuestion;
import org.phillyete.quizzo.domain.Player;
import org.phillyete.quizzo.domain.Quiz;
import org.phillyete.quizzo.web.engine.inmemory.GameStateInvalidException;
import org.phillyete.quizzo.web.engine.inmemory.QuizGameInstance;

import static org.junit.Assert.*;

public class QuizRunTest {

    private final QuizzoTestUtils questionTestUtils = new QuizzoTestUtils();
    private QuizGameInstance quizGameInstance;

    @Before
    public void setUp() {
        Quiz quiz = QuizzoTestUtils.generateTestQuiz();
        quizGameInstance = new QuizGameInstance(quiz, "Test Game");
    }

    @Test
    public void testStartQuizRunToGetFirstQuestion() {
        assertEquals(null, quizGameInstance.getCurrentQuestion());
        quizGameInstance.beginTakingPlayers();

        Player p = QuizzoTestUtils.generateRandomPlayer();
        for (int i = 0; i < 10; i++) {
            quizGameInstance.addPlayer(QuizzoTestUtils.generateRandomPlayer());
        }
        quizGameInstance.beginAskingQuestions();
        assertNotNull(quizGameInstance.getCurrentQuestion());
    }

    @Test(expected = GameStateInvalidException.class)
    public void testStartQuizRunWithNoPlayersShouldFail() {
        quizGameInstance.beginAskingQuestions();
        assertEquals(null, quizGameInstance.getCurrentQuestion());
    }

    @Test(expected = GameStateInvalidException.class)
    public void tryMovingToQuestionBeforeStartingQuiz() {
        quizGameInstance.moveToNextQuestion();
    }


    @Test
    public void testSetInQuestionAnsweringMode() {
        quizGameInstance.beginTakingPlayers();
        quizGameInstance.addPlayer(QuizzoTestUtils.generateRandomPlayer());
        quizGameInstance.beginAskingQuestions();
        MultipleChoiceQuestion question = quizGameInstance.getCurrentQuestion();
        assertNotNull(quizGameInstance.getCurrentQuestion());
        assertTrue(question.getClass().isAssignableFrom(MultipleChoiceQuestion.class));
    }


    @Test
    public void testAddPlayer() {
        quizGameInstance.beginTakingPlayers();
        Player p = new Player("Jones");
        quizGameInstance.addPlayer(p);
    }

    @Test
    public void testStopTakingAnswers() {
        quizGameInstance.beginTakingPlayers();
        quizGameInstance.addPlayer(QuizzoTestUtils.generateRandomPlayer());
        quizGameInstance.beginAskingQuestions();
        quizGameInstance.stopTakingAnswers();
        assertNull(quizGameInstance.getCurrentQuestion());
    }

    @Test(expected = GameStateInvalidException.class)
    public void testInvalidTransitionFromNotStartedToAwaitingAnswer() {
        quizGameInstance.moveToNextQuestion();
    }

}
