package org.phillyete.quizzo.web.engine.inmemory;

import org.phillyete.quizzo.web.engine.GameState;

public class GameStateInvalidException extends RuntimeException {

    public GameStateInvalidException(String message) {
        super(message);
    }

    public GameStateInvalidException(GameState from, GameState to) {
        super("Invalid state transition from " + from + " to " + to);
    }
}
