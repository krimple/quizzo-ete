package org.phillyete.quizzo.engine;

import java.util.HashMap;
import java.util.List;

import org.phillyete.quizzo.domain.MultipleChoiceQuestion;
import org.phillyete.quizzo.domain.PlayerAnswer;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/23/13
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
public interface GameRunEngine {
    boolean gameExists(String gameId);

    // see if a particular quiz is complete
    boolean isQuizRunComplete(String title);

    // run by an admin - returns the game id or null if not found
    String startQuizRunAndBeginTakingPlayers(String quizId, String gameName);

    boolean moveToNextQuestion(String gameId);

    void endQuestion(String gameId);

    void endGame(String gameId);

    void destroyQuizRun(String gameId);

    MultipleChoiceQuestion getCurrentQuizQuestion(String gameId);

    MultipleChoiceQuestion getQuestionByIndex(String gameId, int index);

    AnswerStatus submitPlayerAnswer(PlayerAnswer answer);

    void addPlayer(String gameId, String playerId);

    void stopTakingPlayersAndStartGamePlay(String gameId);

    GameState getGameState(String gameId);

    List<HashMap> getGamesAwaitingPlayers();
}
