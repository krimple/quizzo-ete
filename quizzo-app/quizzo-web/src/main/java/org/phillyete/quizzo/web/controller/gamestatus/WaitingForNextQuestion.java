package org.phillyete.quizzo.web.controller.gamestatus;

public class WaitingForNextQuestion extends GameStatus {


    public WaitingForNextQuestion(int currentScore, String message) {
        super(currentScore);
        this.setStatus("WaitingForNextQuestion");
        this.message = message;
    }

    private String message;

    private Integer lastQuestionIndex;

    private Integer lastQuestionNumber;

    private Character lastQuestionChoice;

    private Integer lastQuestionScore;

    public Integer getLastQuestionIndex() {
        return lastQuestionIndex;
    }

    public void setLastQuestionIndex(Integer lastQuestionIndex) {
        this.lastQuestionIndex = lastQuestionIndex;
    }

    public Integer getLastQuestionNumber() {
        return lastQuestionNumber;
    }

    public void setLastQuestionNumber(Integer lastQuestionNumber) {
        this.lastQuestionNumber = lastQuestionNumber;
    }

    public Character getLastQuestionChoice() {
        return lastQuestionChoice;
    }

    public void setLastQuestionChoice(Character lastQuestionChoice) {
        this.lastQuestionChoice = lastQuestionChoice;
    }

    public Integer getLastQuestionScore() {
        return lastQuestionScore;
    }

    public void setLastQuestionScore(Integer lastQuestionScore) {
        this.lastQuestionScore = lastQuestionScore;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
