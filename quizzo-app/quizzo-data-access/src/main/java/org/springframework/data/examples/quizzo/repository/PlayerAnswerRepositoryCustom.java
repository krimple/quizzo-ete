package org.springframework.data.examples.quizzo.repository;

public interface PlayerAnswerRepositoryCustom {

    int calculateScore(String gameId, String playerName);
}
