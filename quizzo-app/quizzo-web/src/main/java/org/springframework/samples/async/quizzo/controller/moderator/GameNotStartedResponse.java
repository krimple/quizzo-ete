package org.springframework.samples.async.quizzo.controller.moderator;

import org.springframework.samples.async.quizzo.responses.QuizPollResponse;

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
