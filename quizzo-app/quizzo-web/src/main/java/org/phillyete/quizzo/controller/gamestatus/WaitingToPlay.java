package org.phillyete.quizzo.controller.gamestatus;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/24/13
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class WaitingToPlay extends GameStatus {

    public WaitingToPlay(String message) {
        super(0);
        this.setStatus("WaitingToPlay");
        this.message = message;
    }
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
