package org.springframework.samples.async.quizzo.engine;

public enum QuizRunStateEnum {
    NOT_STARTED,
    AWAITING_ANSWER,
    WAIT_NEXT_QUESTION,
    COMPLETE
}
