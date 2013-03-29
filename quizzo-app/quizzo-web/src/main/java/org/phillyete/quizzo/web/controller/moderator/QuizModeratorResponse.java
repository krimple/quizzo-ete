package org.phillyete.quizzo.web.controller.moderator;

public abstract class QuizModeratorResponse {

    private String category;

    protected void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }
}
