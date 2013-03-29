package org.phillyete.quizzo.web.controller.gamestatus;

public class GameComplete extends GameStatus {

    public GameComplete(int score) {
        // TODO get score
        this.setCurrentScore(0);
        this.setStatus("GameComplete");
    }
}
