package org.phillyete.quizzo.web.controller.moderator;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/22/13
 * Time: 10:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameNotStartedResponse extends QuizModeratorResponse {

    public GameNotStartedResponse() {
        super();
        this.setCategory("GameNotStarted");
    }
}
