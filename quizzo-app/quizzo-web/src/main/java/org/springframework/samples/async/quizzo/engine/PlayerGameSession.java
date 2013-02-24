package org.springframework.samples.async.quizzo.engine;

public interface PlayerGameSession {

    String getGameId();
    void setGameId(String gameId);
    String getPlayerId();
    void setPlayerId(String playerId);
}
