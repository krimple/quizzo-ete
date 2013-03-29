package org.phillyete.quizzo.web.controller.gamestatus;

public class GameStatus {

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

    protected void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public String getStatus() {
        return status;
    }

    protected void setStatus(String status) {
        this.status = status;
    }
}
