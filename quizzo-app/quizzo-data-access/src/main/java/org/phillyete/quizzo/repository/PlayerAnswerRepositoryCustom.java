package org.phillyete.quizzo.repository;

public interface PlayerAnswerRepositoryCustom {

    int calculateScore(String gameId, String playerName);
}
