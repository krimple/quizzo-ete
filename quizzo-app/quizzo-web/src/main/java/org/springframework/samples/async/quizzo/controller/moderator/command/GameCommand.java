package org.springframework.samples.async.quizzo.controller.moderator.command;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/28/13
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GameCommand extends ModeratorCommand {

    private String gameId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
