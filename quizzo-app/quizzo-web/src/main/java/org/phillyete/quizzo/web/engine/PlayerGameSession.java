package org.phillyete.quizzo.web.engine;

public interface PlayerGameSession {

    String getGameId();
    void setGameId(String gameId);
    String getPlayerId();
    void setPlayerId(String playerId);
}
