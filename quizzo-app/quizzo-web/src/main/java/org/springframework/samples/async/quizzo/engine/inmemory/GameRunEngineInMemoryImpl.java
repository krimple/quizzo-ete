package org.springframework.samples.async.quizzo.engine.inmemory;

import org.springframework.data.examples.quizzo.domain.*;
import org.springframework.data.examples.quizzo.repository.PlayerAnswerRepository;
import org.springframework.data.examples.quizzo.repository.PlayerRepository;
import org.springframework.data.examples.quizzo.repository.QuizRepository;
import org.springframework.samples.async.quizzo.engine.AnswerStatus;
import org.springframework.samples.async.quizzo.engine.GameRunEngine;
import org.springframework.samples.async.quizzo.engine.GameState;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameRunEngineInMemoryImpl implements GameRunEngine {

    private QuizRepository quizRepository;

    private PlayerRepository playerRepository;

    private PlayerAnswerRepository playerAnswerRepository;

    /** stores our quiz runs */
    final ConcurrentHashMap<String, QuizGameInstance> gameInstances;

    public GameRunEngineInMemoryImpl(QuizRepository quizRepository,
                                     PlayerRepository playerRepository,
                                     PlayerAnswerRepository playerAnswerRepository) {

        this.quizRepository = quizRepository;
        this.playerRepository = playerRepository;
        this.playerAnswerRepository = playerAnswerRepository;
        this.gameInstances = new ConcurrentHashMap<String, QuizGameInstance>();
    }

    @Override
    public boolean gameExists(String gameId) {
        // probably needs to be more efficient than this...
        // very expensive. We need an existence repo method...
        return gameInstances.containsKey(gameId);
    }

    // see if a particular quiz is complete
    @Override
    public boolean isQuizRunComplete(String title) {
        QuizGameInstance quizGameInstance = getQuizRun(title);
        return quizGameInstance.isQuizRunComplete();
    }


    // run by an admin - returns the game id
    @Override
    public String startQuizRunAndBeginTakingPlayers(String quizId, String gameName) {
        Quiz quiz = quizRepository.findOne(quizId);
        if (quiz == null) {
            return null;
        } else {
            QuizGameInstance quizGameInstance = new QuizGameInstance(quiz, gameName);
            quizGameInstance.beginTakingPlayers();
            // the system refers to the game based on a UUID
            String gameId = quizGameInstance.getGameId();
            gameInstances.put(gameId, quizGameInstance);
            return gameId;
        }
    }

    @Override
    public void stopTakingPlayersAndStartGamePlay(String gameId) {
        QuizGameInstance quizGameInstance = getQuizRun(gameId);
        quizGameInstance.beginAskingQuestions();
    }

    @Override
    public boolean moveToNextQuestion(String gameId) {
        // this is only called by admin users
        QuizGameInstance quizGameInstance = getQuizRun(gameId);
        return quizGameInstance.moveToNextQuestion();
    }

    @Override
    public void endQuestion(String gameId) {
        QuizGameInstance quizGameInstance = getQuizRun(gameId);
        quizGameInstance.stopTakingAnswers();
    }

    @Override
    public void endQuiz(String gameId) {
        QuizGameInstance quizGameInstance = getQuizRun(gameId);
        quizGameInstance.endQuiz();

    }
    @Override
    public void destroyQuizRun(String gameId) {
        if (this.gameInstances.containsKey(gameId)) {
            // c ya
            this.gameInstances.remove(gameId);
        }
    }

    @Override
    public MultipleChoiceQuestion getCurrentQuizQuestion(String gameId) {
        QuizGameInstance quizGameInstance = getQuizRun(gameId);
        return quizGameInstance.getCurrentQuestion();
    }

  @Override
  public MultipleChoiceQuestion getQuestionByIndex(String gameId, int index) {
    MultipleChoiceQuestion question = gameInstances.get(gameId).getQuestionByIndex(index);
    return question;
  }


  QuizGameInstance getQuizRun(String gameId) {
        if (!gameExists(gameId)) {
            throw new RuntimeException("No quiz run in progress with this title or quiz removed.");
        }
        return gameInstances.get(gameId);
    }

    @Override
    public void addPlayer(String gameId, String playerId) {
        QuizGameInstance quizGameInstance = getQuizRun(gameId);
        Player player = playerRepository.findOneByName(playerId);
        quizGameInstance.addPlayer(player);
    }

    @Override
    public AnswerStatus submitPlayerAnswer(PlayerAnswer answer) {
        Assert.notNull(answer, "answer cannot be null");
        Assert.notNull(answer.getPlayerId(), "answer contains a null player ID");
        Assert.notNull(answer.getQuizId(), "quiz id must be present");
        Assert.notNull(answer.getGameId(), "game id must be present");
        Assert.notNull(answer.getChoice(), "choice must be specified.");

        Player player = playerRepository.findOne(answer.getPlayerId());
        String gameId = answer.getGameId();
        MultipleChoiceQuestion currentQuestion = getCurrentQuizQuestion(gameId);
        int answerQuestionNumber = answer.getQuestionNumber();
        int currentQuestionNumber = getCurrentQuizQuestion(gameId).getQuestionNumber();

        if (player == null) {
            return AnswerStatus.PLAYER_NOT_REGISTERED;
        }
        if (!gameExists(answer.getGameId())) {
            return AnswerStatus.NO_GAME_IN_PROGRESS;
        }
        if (answerQuestionNumber < currentQuestionNumber) {
            return AnswerStatus.QUESTION_EXPIRED;
        }
        if (answerQuestionNumber > currentQuestionNumber) {
            return AnswerStatus.INVALID_QUESTION_NUMBER;
        }

        if (!currentQuestion.getChoices().keySet().contains(
                Choice.Letter.fromChar(answer.getChoice()))) {
            return AnswerStatus.INVALID_CHOICE;
        }

        // TODO - currently these are in a singleton

        if (playerAnswerRepository.findByGameIdAndPlayerIdAndQuestionNumber(
                answer.getGameId(), answer.getPlayerId(),
                answer.getQuestionNumber()) != null) {
            return AnswerStatus.DUPLICATE_ANSWER;
        }

        playerAnswerRepository.save(answer);
        return AnswerStatus.ANSWER_SUBMITTED;
    }

    @Override
    public GameState getGameState(String gameId) {
        QuizGameInstance quizGameInstance = getQuizRun(gameId);
        return quizGameInstance.getGameState();
    }

    @Override
    public List<HashMap> getGamesAwaitingPlayers() {
        // TODO - more formal types this is a kludge
        List<HashMap> results = new ArrayList<HashMap>();
        Iterator<Map.Entry<String, QuizGameInstance>> entryIterator = gameInstances.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, QuizGameInstance> entry = entryIterator.next();
            if (entry.getValue().getGameState().equals(GameState.AWAITING_PLAYERS)) {
                HashMap result = new HashMap();
                result.put("gameId", entry.getKey());
                // note - leaky abstraction - should expose this as an interface method perhaps
                result.put("title", entry.getValue().getQuizTitle());
                results.add(result);
            }
        }
        return results;
    }


}
