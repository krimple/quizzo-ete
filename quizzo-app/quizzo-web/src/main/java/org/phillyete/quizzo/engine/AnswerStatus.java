package org.phillyete.quizzo.engine;

public enum AnswerStatus {
    NO_GAME_IN_PROGRESS("No game in progress..."),
    INVALID_QUESTION_NUMBER("Invalid question #"),
    INVALID_CHOICE("Invalid choice for question"),
    QUESTION_EXPIRED("Question has expired"),
    PLAYER_NOT_REGISTERED("This player is not registered"),
    DUPLICATE_ANSWER("Duplicate answer"),
    ANSWER_SUBMITTED("Answer submitted");

    private AnswerStatus(String message) {
        this.message = message;
    }

    private String message;

    public String toString() {
        return message;
    }
}
