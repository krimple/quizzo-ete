package org.springframework.samples.async.quizzo.engine.inmemory;

import org.springframework.samples.async.quizzo.engine.GameState;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/23/13
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameStateInvalidException extends RuntimeException {

    public GameStateInvalidException(String message) {
        super(message);
    }

    public GameStateInvalidException(GameState from, GameState to) {
        super("Invalid state transition from " + from + " to " + to);
    }
}
