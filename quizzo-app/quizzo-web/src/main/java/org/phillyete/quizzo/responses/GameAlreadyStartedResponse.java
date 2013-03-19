package org.phillyete.quizzo.responses;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/22/13
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameAlreadyStartedResponse extends QuizPollResponse {
    public GameAlreadyStartedResponse() {
        super();
        this.setCategory("GameAlreadyStarted");
    }
}
