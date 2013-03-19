package org.phillyete.quizzo.controller.gamestatus;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/24/13
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */

public class GameStatus {

    private int currentScore;

    private String status;

    public GameStatus() {

    }

    public GameStatus(int currentScore) {
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
