package org.phillyete.quizzo.responses;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/23/13
 * Time: 12:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class AnswerSubmittedResponse extends QuizPollResponse {

    public AnswerSubmittedResponse() {
        super();
        this.setCategory("AnswerSubmitted");
    }
}
