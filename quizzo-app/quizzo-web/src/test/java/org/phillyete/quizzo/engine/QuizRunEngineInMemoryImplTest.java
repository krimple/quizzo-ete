package org.phillyete.quizzo.engine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
import org.phillyete.quizzo.domain.*;
import org.phillyete.quizzo.engine.AnswerStatus;
import org.phillyete.quizzo.engine.inmemory.GameRunEngineInMemoryImpl;
import org.phillyete.quizzo.repository.PlayerAnswerRepository;
import org.phillyete.quizzo.repository.PlayerRepository;
import org.phillyete.quizzo.repository.QuizRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class QuizRunEngineInMemoryImplTest {

    private GameRunEngineInMemoryImpl gameRunEngine;

    private QuizRepository mockQuizRepository;

    private PlayerRepository mockPlayerRepository;

    private PlayerAnswerRepository mockPlayerAnswerRepository;


    @Before
    public void setUp() {
        // let's make a mockery of it...
        mockQuizRepository = Mockito.mock(QuizRepository.class);
        mockPlayerRepository = Mockito.mock(PlayerRepository.class);
        mockPlayerAnswerRepository = Mockito.mock(PlayerAnswerRepository.class);
        gameRunEngine = new GameRunEngineInMemoryImpl(mockQuizRepository, mockPlayerRepository, mockPlayerAnswerRepository);
    }


    @Test
    public void testGameExists() {
        Quiz fakeQuiz = QuizzoTestUtils.generateTestQuiz();
        when(mockQuizRepository.findOne(fakeQuiz.getId())).thenReturn(fakeQuiz);

        List<MultipleChoiceQuestion> questions = new ArrayList<MultipleChoiceQuestion>();
        List<Game> gamesPlayed = new ArrayList<Game>();
        String gameId = gameRunEngine.startQuizRunAndBeginTakingPlayers(fakeQuiz.getId(), "demoGame");
        assertTrue(gameRunEngine.gameExists(gameId));
    }

    @Test
    public void testGameNotExists() {
        Assert.assertFalse(gameRunEngine.gameExists("werenotinhere"));
    }

    @Test
    public void answerQuestion() {
        Quiz quiz = QuizzoTestUtils.generateTestQuiz();

        Player fakePlayer = QuizzoTestUtils.generateRandomPlayer();

        when(mockQuizRepository.findOne(quiz.getId())).thenReturn(quiz);

        when(mockPlayerRepository.findOne(fakePlayer.getName()))
                .thenReturn(fakePlayer);

        String gameId = gameRunEngine.startQuizRunAndBeginTakingPlayers(quiz.getId(), "test-run");

        when(mockPlayerRepository.findOneByName(
                fakePlayer.getName())).thenReturn(fakePlayer);

        gameRunEngine.addPlayer(gameId, fakePlayer.getName());

        // admin move, assume one user
        gameRunEngine.moveToNextQuestion(gameId);

        MultipleChoiceQuestion question = gameRunEngine.getCurrentQuizQuestion(gameId);

        assertNotNull(question);

        AnswerStatus status = gameRunEngine.submitPlayerAnswer(
                new PlayerAnswer(fakePlayer.getName(),
                        gameId, "dumbid",
                        0, 'a', 10));

        assertEquals(AnswerStatus.ANSWER_SUBMITTED, status);
    }
}
