package org.springframework.samples.async.quizzo.engine;

import org.springframework.data.examples.quizzo.domain.*;
import org.springframework.data.examples.quizzo.repository.PlayerAnswerRepository;
import org.springframework.data.examples.quizzo.repository.PlayerRepository;
import org.springframework.data.examples.quizzo.repository.QuizRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class QuizRunEngine {

    public static enum AnswerStatus {
        NO_GAME_INPROGRESS,
        INVALID_QUESTION_NUMBER,
        INVALID_CHOICE,
        QUESTION_EXPIRED,
        PLAYER_NOT_REGISTERED,
        DUPLICATE_ANSWER,
        ANSWER_SUBMITTED
    }

    private QuizRepository quizRepository;

    private PlayerRepository playerRepository;

    private PlayerAnswerRepository playerAnswerRepository;

    /** stores our quiz runs */
    private final ConcurrentHashMap<String, QuizRun> quizRuns;

    public QuizRunEngine(QuizRepository quizRepository,
                         PlayerRepository playerRepository,
                         PlayerAnswerRepository playerAnswerRepository) {

        this.quizRepository = quizRepository;
        this.playerRepository = playerRepository;
        this.playerAnswerRepository = playerAnswerRepository;
        this.quizRuns = new ConcurrentHashMap<String, QuizRun>();
    }

    public boolean gameExists(String gameId) {
        // probably needs to be more efficient than this...
        // very expensive. We need an existence repo method...
        return quizRuns.containsKey(gameId);
    }

    // see if a particular quiz is complete
    public boolean isQuizRunComplete(String title) {
        QuizRun quizRun = getQuizRun(title);
        return quizRun.isQuizRunComplete();
    }


    // run by an admin - returns the game id
    public String startQuizRun(String quizId, String gameName) {
        Quiz quiz = quizRepository.findOne(quizId);
        if (quiz == null) {
            throw new RuntimeException("only call this once you've searched for the title in the controller/service...");
        } else {
            QuizRun quizRun = new QuizRun(quiz, gameName);
            String gameId = quizRun.getGameId();
            quizRuns.put(gameId, quizRun);
            return gameId;
        }
    }

    public boolean moveToNextQuestion(String gameId) {
        // this is only called by admin users
        QuizRun quizRun = getQuizRun(gameId);
        return quizRun.moveToNextQuestion();
    }

    public void endQuestion(String gameId) {
        QuizRun quizRun = getQuizRun(gameId);
        quizRun.stopTakingAnswers();
    }

    public void endQuiz(String gameId) {
        QuizRun quizRun = getQuizRun(gameId);
        quizRun.endQuiz();

    }
    public void destroyQuizRun(String gameId) {
        if (this.quizRuns.containsKey(gameId)) {
            // c ya
            this.quizRuns.remove(gameId);
        }
    }

    public MultipleChoiceQuestion getCurrentQuizQuestion(String gameId) {
        QuizRun quizRun = getQuizRun(gameId);
        return quizRun.getCurrentQuestion();
    }

    public QuizRun getQuizRun(String gameId) {
        if (!gameExists(gameId)) {
            throw new RuntimeException("No quiz run in progress with this title or quiz removed.");
        }
        return quizRuns.get(gameId);
    }

    public AnswerStatus submitPlayerAnswer(PlayerAnswer answer,
                                           String quizId,
                                           String gameId) {
        Assert.notNull(answer, "answer cannot be null");
        Assert.notNull(answer.getPlayerId(), "answer contains a null player ID");
        Assert.notNull(answer.getQuizId(), "quiz id must be present");
        Assert.notNull(answer.getGameId(), "game id must be present");
        Assert.notNull(answer.getChoice(), "choice must be specified.");

        Player player = playerRepository.findOne(answer.getPlayerId());
        MultipleChoiceQuestion currentQuestion = getCurrentQuizQuestion(quizId);
        int answerQuestionNumber = answer.getQuestionNumber();
        int currentQuestionNumber = getCurrentQuizQuestion(quizId).getQuestionNumber();


        if (player == null) {
            return AnswerStatus.PLAYER_NOT_REGISTERED;
        }
        if (!gameExists(answer.getGameId())) {
            return AnswerStatus.NO_GAME_INPROGRESS;
        }
        if (answerQuestionNumber < currentQuestionNumber) {
            return AnswerStatus.QUESTION_EXPIRED;
        }
        if (answerQuestionNumber > currentQuestionNumber) {
            return AnswerStatus.INVALID_QUESTION_NUMBER;
        }

        if (currentQuestion.getChoices().keySet().contains(
                Choice.Letter.fromChar(answer.getChoice()))) {
            return AnswerStatus.INVALID_CHOICE;
        }

        // TODO - currently these are in a singleton

        answer.setQuizId(quizId);
        answer.setGameId(gameId);
        if (playerAnswerRepository.findByGameIdAndPlayerIdAndQuestionNumber(
                answer.getGameId(), answer.getPlayerId(),
                answer.getQuestionNumber()) != null) {
            return AnswerStatus.DUPLICATE_ANSWER;
        }

        playerAnswerRepository.save(answer);
        return AnswerStatus.ANSWER_SUBMITTED;
    }




}
