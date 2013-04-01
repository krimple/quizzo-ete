package org.phillyete.quizzo.web.controller.gamestatus;

public class GameStatus {

    private String playerNickName;

    private String gameId;

    private String gameTitle;

    private int currentScore;

    private String status;

    public GameStatus() {
      this.setStatus(this.getClass().getSimpleName());
    }

    public GameStatus(int currentScore) {
        this();
        this.currentScore = currentScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public String getStatus() {
        return status;
    }

    protected void setStatus(String status) {
        this.status = status;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getPlayerNickName() {
        return playerNickName;
    }

    public void setPlayerNickName(String playerNickName) {
        this.playerNickName = playerNickName;
    }
}
