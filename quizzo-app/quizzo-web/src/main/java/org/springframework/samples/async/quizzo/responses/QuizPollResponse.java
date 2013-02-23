package org.springframework.samples.async.quizzo.responses;

public abstract class QuizPollResponse {

    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private String category;

    protected void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }
}
