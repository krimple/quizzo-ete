package org.phillyete.quizzo.responses;

import org.phillyete.quizzo.domain.MultipleChoiceQuestion;

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
