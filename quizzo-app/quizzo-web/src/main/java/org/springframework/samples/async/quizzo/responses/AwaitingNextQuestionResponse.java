package org.springframework.samples.async.quizzo.responses;

import org.springframework.data.examples.quizzo.domain.Choice;
import org.springframework.data.examples.quizzo.domain.PlayerAnswer;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/22/13
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class AwaitingNextQuestionResponse extends QuizPollResponse {

    public AwaitingNextQuestionResponse() {
        super();
        this.setCategory("AwaitingQuestion");
        this.setMessage("You're gonna feel this next question in your gut!");
    }

    private String message;

    private PlayerAnswer lastAnswer;

    private Choice.Letter correctAnswer;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PlayerAnswer getLastAnswer() {
        return lastAnswer;
    }

    public void setLastAnswer(PlayerAnswer lastAnswer) {
        this.lastAnswer = lastAnswer;
    }

    public Choice.Letter getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Choice.Letter correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
