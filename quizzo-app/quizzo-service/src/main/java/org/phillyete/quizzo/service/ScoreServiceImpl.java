package org.phillyete.quizzo.service;

import org.phillyete.quizzo.domain.PlayerAnswer;
import org.phillyete.quizzo.domain.Quiz;
import org.phillyete.quizzo.repository.PlayerAnswerRepository;
import org.phillyete.quizzo.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreServiceImpl implements ScoreService  {

    private final QuizRepository quizRepository;
    private final PlayerAnswerRepository playerAnswerRepository;

    @Autowired
    public ScoreServiceImpl(PlayerAnswerRepository playerAnswerRepository, QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
        this.playerAnswerRepository = playerAnswerRepository;
    }
    /**
     * Compute total scores for a quizzo game by player
     * @param gameId the game instance
     * @return a Map of {@link ScoreSummary} indexed by player Id
     */
    public Map<String, ScoreSummary> getTotalScores(String gameId) {
        Map<String, ScoreSummary> scores = new HashMap<String, ScoreSummary>();
        List<PlayerAnswer> answers = playerAnswerRepository.findByGameId(gameId);
        if (!CollectionUtils.isEmpty(answers)) {

            Quiz quiz = quizRepository.findOne(answers.get(0).getQuizId());

            for (PlayerAnswer answer : answers) {
                int questionScore = quiz.getScore(answer);
                if (!scores.containsKey(answer.getPlayerId())) {
                    ScoreSummary summary = new ScoreSummary(answer);
                    scores.put(answer.getPlayerId(), summary);
                }
                scores.get(answer.getPlayerId()).incrementScoreBy(questionScore);
            }

        }
        return scores;
    }

    /**
     * Get score details for a specific question in a quizzo quiz
     * @param gameId the game instance
     * @param questionNumber the question number
     * @return a list of {@link ScoreDetail}
     */
    public List<ScoreDetail> getScoresForQuestion(String gameId, int questionNumber) {
        List<ScoreDetail> scores = new ArrayList<ScoreDetail>();
        List<PlayerAnswer> answers = playerAnswerRepository.findByGameIdAndQuestionNumber(gameId, questionNumber);
        if (!CollectionUtils.isEmpty(answers)) {

            Quiz quiz = quizRepository.findOne(answers.get(0).getQuizId());

            for (PlayerAnswer answer : answers) {
                ScoreDetail detail = new ScoreDetail(answer);
                detail.setScore(quiz.getScore(answer));
                scores.add(detail);
            }
        }
        return scores;
    }
}
