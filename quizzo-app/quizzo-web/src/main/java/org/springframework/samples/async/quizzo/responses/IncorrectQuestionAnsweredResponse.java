package org.springframework.samples.async.quizzo.responses;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/28/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class IncorrectQuestionAnsweredResponse extends QuizPollResponse {

    public IncorrectQuestionAnsweredResponse() {
        setCategory("IncorrectQuestionAnswered");
    }

}
