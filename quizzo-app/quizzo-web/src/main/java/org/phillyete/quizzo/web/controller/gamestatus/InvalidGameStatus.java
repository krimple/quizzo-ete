package org.phillyete.quizzo.web.controller.gamestatus;

public class InvalidGameStatus extends GameStatus {

    public InvalidGameStatus() {
        super(0);
        setStatus("InvalidGame");
    }
}
