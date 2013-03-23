package org.phillyete.quizzo.web.engine;

import java.io.Serializable;

/**
 * Kept in HTTP session to track current player's current game. Once the player joins the game,
 * this is no longer required to be passed between the client and server.  Keeps certain HACKERS
 * from messing up my game, ya see... (you have been warned, Don :)
 *
 * Yes, it makes it stateful but also opens the door to other possibilities...
 *
 * We're using this with a Scoped proxy to inject the user's HttpSession-scoped object into the quiz run
 * controller.
 *
 */
public class PlayerGameSessionImpl implements Serializable, PlayerGameSession {

    private String gameId;

    private String playerId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}

