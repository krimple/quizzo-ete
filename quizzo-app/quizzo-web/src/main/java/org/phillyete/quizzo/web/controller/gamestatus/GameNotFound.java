package org.phillyete.quizzo.web.controller.gamestatus;

public class GameNotFound extends GameStatus {

  private String gameId;

  public GameNotFound() {
    super(0);
    // TODO - rip out another status for not spec or good enough for now?
    this.setStatus("GameNotFound");
  }

  public GameNotFound(String gameId) {
    super(0);
    this.setGameId(gameId);
    this.setCurrentScore(0);
    this.setStatus("GameNotFound");
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }
}