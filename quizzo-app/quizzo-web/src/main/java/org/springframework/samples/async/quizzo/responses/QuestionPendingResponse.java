package org.springframework.samples.async.quizzo.responses;

import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;

public class QuestionPendingResponse extends QuizPollResponse {

    public QuestionPendingResponse(MultipleChoiceQuestion question) {
        super();
        setCategory("QuestionPending");
        this.question = question;
    }

    public QuestionPendingResponse() {
        super();
    }

    private MultipleChoiceQuestion question;

    public MultipleChoiceQuestion getQuestion() {
        return question;
    }

    public void setQuestion(MultipleChoiceQuestion question) {
        this.question = question;
    }
}
