package org.phillyete.quizzo.web.controller.gamestatus;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/24/13
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameComplete extends GameStatus {

    public GameComplete(int score) {
        // TODO get score
        this.setCurrentScore(0);
        this.setStatus("GameComplete");
    }
}
