package org.phillyete.quizzo.web.controller.gamestatus;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/24/13
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidGameStatus extends GameStatus {

    public InvalidGameStatus() {
        super(0);
        setStatus("InvalidGame");
    }
}
