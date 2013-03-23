package org.phillyete.quizzo.web.controller.moderator;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/22/13
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameStartedResponse extends QuizModeratorResponse {

  public GameStartedResponse(String gameId) {
    super();
    this.setCategory("GameStarted");
    this.setGameId(gameId);
  }

  private String gameId;

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }
}
