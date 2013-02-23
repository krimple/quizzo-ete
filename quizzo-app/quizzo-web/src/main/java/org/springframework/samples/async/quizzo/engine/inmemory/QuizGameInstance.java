package org.springframework.samples.async.quizzo.engine.inmemory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.examples.quizzo.domain.Game;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.Player;
import org.springframework.data.examples.quizzo.domain.Quiz;
import org.springframework.samples.async.quizzo.engine.GameState;

import java.util.*;

public class QuizGameInstance {

    private static Log logger = LogFactory.getLog(QuizGameInstance.class);

    private final Quiz quiz;

    private final Game game;

    private Set<Player> players;

    private GameState gameState;

    // starting quiz doesn't change this,
    // but each ask of the next question bumps up by 1
    private int currentQuestionIndex = 0;

    public QuizGameInstance(Quiz quiz, String gameId) {
        this.quiz = quiz;
        this.game = new Game(gameId);
        // keep 'em organized by player name
        this.players = new TreeSet<Player>();
        this.quiz.getGamesPlayed().add(this.game);
        this.setGameState(GameState.NOT_STARTED);
    }


    public void beginTakingPlayers() {
        // denotes whether we've actually started the game... can't be as easily
        // derived by state movement...
        currentQuestionIndex = 0;
        setGameState(GameState.AWAITING_PLAYERS);
    }

    public void beginAskingQuestions() {
        // todo - maybe down with state checks?
        if (players.size() == 0) {
            throw new GameStateInvalidException("You can't have a game without players!");
        }
        setGameState(GameState.AWAITING_ANSWER);

    }

    public MultipleChoiceQuestion getCurrentQuestion() {
        if (gameState.equals(GameState.AWAITING_ANSWER) &&
            currentQuestionIndex < quiz.getQuestions().size()) {
            return quiz.getQuestions().get(currentQuestionIndex);
        } else {
            // no answer for you right now... find out why...
            logger.debug("not in correct state." + this.toString());
            return null;
        }
    }

    public void addPlayer(Player p) {
        // TODO - guard against players going into the game after it starts?
        if (gameState != GameState.AWAITING_PLAYERS) {
            throw new GameStateInvalidException("Cannot add players once game is underway...");
        }

        // ok, just add...
        if (!players.contains(p)) {
            players.add(p);
        }
    }

    public void stopTakingAnswers() {
        if (gameState.equals(GameState.AWAITING_ANSWER)) {
            logger.debug("Now suspending answers until the next question");
            this.setGameState(GameState.WAIT_NEXT_QUESTION);
        } else {
            logger.debug("Cannot stop taking answers unless we're already awaiting answers...");
        }

    }

    /** Jump to next question. Returns false if we are out of questions. */
    public boolean moveToNextQuestion() {

        this.setGameState(GameState.AWAITING_ANSWER);
        if ((currentQuestionIndex + 1) < this.quiz.getQuestions().size()) {
            currentQuestionIndex++;
            return true;
        } else {
            logger.debug("no more questions... ending quiz.");
            this.setGameState(GameState.COMPLETE);
            return false;
        }
    }

    public void endQuiz() {
        this.setGameState(GameState.COMPLETE);
    }

    private synchronized void setGameState(GameState newQuizState) {
        // guard state transitions

        if (this.gameState == null) {
            if (!newQuizState.equals(GameState.NOT_STARTED)) {
                throw new GameStateInvalidException("Cannot start with any state other than NOT_STARTED");
            } else {
                // hack to get past other tests that require a prior state...
                this.gameState = GameState.NOT_STARTED;
                return;
            }
        }
        if (this.gameState.equals(GameState.NOT_STARTED)) {
            if (!newQuizState.equals(GameState.AWAITING_PLAYERS)) {
                throw new GameStateInvalidException(gameState, newQuizState);
            }
        }
        if (this.gameState.equals(GameState.AWAITING_PLAYERS)) {
            if (!newQuizState.equals(GameState.AWAITING_ANSWER) &&
                    !newQuizState.equals(GameState.COMPLETE)) {
                throw new GameStateInvalidException(gameState, newQuizState);
            }
        }

        if (this.gameState == GameState.AWAITING_ANSWER) {
            if (newQuizState != GameState.COMPLETE &&
                newQuizState != GameState.WAIT_NEXT_QUESTION) {
                throw new GameStateInvalidException(gameState, newQuizState);
            }
        }

        if (this.gameState == GameState.WAIT_NEXT_QUESTION) {
            if (newQuizState != GameState.AWAITING_ANSWER &&
                newQuizState != GameState.COMPLETE) {
                throw new GameStateInvalidException(gameState, newQuizState);
            }
        }

        if (this.gameState == GameState.COMPLETE) {
            throw new GameStateInvalidException("Game over. Cannot change states.");
        }

        this.gameState = newQuizState;
    }

    public boolean isQuizRunComplete() {
        return gameState.equals(GameState.COMPLETE);
    }

    public String getGameId() {
        return this.game.getId();
    }

}
