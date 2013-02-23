package org.springframework.samples.async.quizzo.engine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.examples.quizzo.domain.Game;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.Player;
import org.springframework.data.examples.quizzo.domain.Quiz;

import java.util.List;
import java.util.Set;

public class QuizRun {

    private static Log logger = LogFactory.getLog(QuizRun.class);

    private final Quiz quiz;

    private final Game game;

    private Set<Player> players;

    private QuizRunStateEnum quizRunState;

    // each ask of the question bumps up by 1, we start behind the questions
    private int currentQuestionIndex = -1;

    public MultipleChoiceQuestion getCurrentQuestion() {

        List<MultipleChoiceQuestion> questions = quiz.getQuestions();

        if (quizRunState.equals(QuizRunStateEnum.AWAITING_ANSWER) &&
            currentQuestionIndex < quiz.getQuestions().size()) {
            return quiz.getQuestions().get(currentQuestionIndex);
        } else {
            // no answer for you right now... find out why...
            logger.debug("not in correct state." + this.toString());
            return null;
        }
    }

    public QuizRun(Quiz quiz, String gameName) {
        this.quiz = quiz;
        this.game = new Game(gameName);
        this.quiz.getGamesPlayed().add(this.game);
        this.setQuizRunState(QuizRunStateEnum.WAIT_NEXT_QUESTION);
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public void stopTakingAnswers() {
        if (quizRunState.equals(QuizRunStateEnum.AWAITING_ANSWER)) {
            logger.debug("Now suspending answers until the next question");
            this.setQuizRunState(QuizRunStateEnum.WAIT_NEXT_QUESTION);
        } else {
            logger.debug("Cannot stop taking answers unless we're already awaiting answers...");
        }

    }

    /** Jump to next question. Returns false if we are out of questions. */
    public boolean moveToNextQuestion() {
        if ((currentQuestionIndex + 1) < this.quiz.getQuestions().size()) {
            currentQuestionIndex++;
            this.setQuizRunState(QuizRunStateEnum.AWAITING_ANSWER);
            return true;
        } else {
            logger.debug("no more questions... ending quiz.");
            this.setQuizRunState(QuizRunStateEnum.COMPLETE);
            return false;
        }
    }

    public void endQuiz() {
        this.setQuizRunState(QuizRunStateEnum.COMPLETE);
    }

    private synchronized void setQuizRunState(QuizRunStateEnum newQuizState) {
        // guard state transitions
        if (this.quizRunState == QuizRunStateEnum.NOT_STARTED) {
            if (newQuizState != QuizRunStateEnum.AWAITING_ANSWER && newQuizState != QuizRunStateEnum.COMPLETE) {
                throw new RuntimeException("Cannot move from NOT_STARTED to the state of " + newQuizState);
            }
        }

        if (this.quizRunState == QuizRunStateEnum.AWAITING_ANSWER) {
            if (newQuizState != QuizRunStateEnum.COMPLETE && newQuizState == QuizRunStateEnum.WAIT_NEXT_QUESTION) {
                throw new RuntimeException("Cannot move from AWAITING_ANSWER to the state of " + newQuizState);
            }
        }

        if (this.quizRunState == QuizRunStateEnum.WAIT_NEXT_QUESTION) {
            if (newQuizState != QuizRunStateEnum.AWAITING_ANSWER && newQuizState != QuizRunStateEnum.COMPLETE) {
                throw new RuntimeException("Cannot move from WAIT_NEXT_QUESTION to the state of " + newQuizState);
            }
        }

        if (this.quizRunState == QuizRunStateEnum.COMPLETE) {
            throw new RuntimeException("Game over. Cannot change states.");
        }

        this.quizRunState = newQuizState;
    }

    public boolean isQuizRunComplete() {
        return quizRunState.equals(QuizRunStateEnum.COMPLETE);
    }

    public String getGameId() {
        return this.game.getId();
    }

}
