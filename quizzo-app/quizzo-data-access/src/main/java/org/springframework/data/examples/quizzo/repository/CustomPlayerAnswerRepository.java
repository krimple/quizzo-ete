package org.springframework.data.examples.quizzo.repository;

public interface CustomPlayerAnswerRepository {

    int calculateScoreForPlayerAndGame(String gameId, String playerName);
}
