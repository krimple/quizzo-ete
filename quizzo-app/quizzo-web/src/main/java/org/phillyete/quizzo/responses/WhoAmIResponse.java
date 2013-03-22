package org.phillyete.quizzo.responses;

import org.phillyete.quizzo.engine.PlayerGameSession;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 3/21/13
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class WhoAmIResponse extends QuizPollResponse {

    private String playerNickName;
    private String gameId;

    public WhoAmIResponse(PlayerGameSession session) {
        setCategory("WhoAmI");
        this.playerNickName = session.getPlayerId();
        this.gameId = session.getGameId();
    }

    public String getPlayerNickName() {
        return playerNickName;
    }

    public String getGameId() {
        return gameId;
    }
}
