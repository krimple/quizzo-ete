package org.springframework.samples.async.quizzo.controller.gamestatus;

import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/24/13
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class WaitingForAnswer extends GameStatus {


    private MultipleChoiceQuestion question;

    public WaitingForAnswer(int score, MultipleChoiceQuestion question) {
        super(score);
        this.question = question;
        setStatus("WaitingForAnswer");
        setCurrentScore(score);
    }

    public MultipleChoiceQuestion getQuestion() {
        return question;
    }
}
