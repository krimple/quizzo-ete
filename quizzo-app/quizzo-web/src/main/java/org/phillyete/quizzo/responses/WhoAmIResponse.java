package org.phillyete.quizzo.responses;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 3/25/13
 * Time: 9:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class WhoAmIResponse extends QuizPollResponse {

    private String playerNickName;
    private String gameId;

    public WhoAmIResponse(String playerNickName, String gameId) {
        this.setCategory("WhoAmI");
        this.playerNickName = playerNickName;
        this.gameId = gameId;
    }

    public String getPlayerNickName() {
        return playerNickName;
    }

    public String getGameId() {
        return gameId;
    }
}
