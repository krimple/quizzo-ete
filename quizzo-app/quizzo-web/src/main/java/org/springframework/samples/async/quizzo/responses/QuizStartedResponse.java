package org.springframework.samples.async.quizzo.responses;

import org.springframework.samples.async.quizzo.responses.QuizPollResponse;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/22/13
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class QuizStartedResponse extends QuizPollResponse {

    public QuizStartedResponse() {
        super();
        this.setCategory("QuizStarted");
    }
}
