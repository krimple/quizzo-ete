package org.springframework.samples.async.quizzo.engine;

public enum GameState {
    NOT_STARTED("Quiz not started"),
    AWAITING_PLAYERS("Awaiting players"),
    AWAITING_ANSWER("Awaiting answers from players"),
    WAIT_NEXT_QUESTION("Waiting for next question"),
    COMPLETE("Quiz complete");

    private String label;

    private GameState(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }


}
