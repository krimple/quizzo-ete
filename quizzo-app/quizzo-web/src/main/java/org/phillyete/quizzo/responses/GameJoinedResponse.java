package org.phillyete.quizzo.responses;

public class GameJoinedResponse extends QuizPollResponse {

  private String gameId;
  private String title;

  public GameJoinedResponse(String gameId, String title) {
    super();
    this.setCategory("GameJoined");
    this.gameId = gameId;
    this.title = title;
  }

  public String getGameId() {
    return gameId;
  }

  public String getTitle() {
    return title;
  }
}
