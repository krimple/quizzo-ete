package org.springframework.samples.async.quizzo.engine;

import org.springframework.data.examples.quizzo.domain.Game;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.Player;
import org.springframework.data.examples.quizzo.domain.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuizzoTestUtils {
    public QuizzoTestUtils() {
    }

    static Quiz generateTestQuiz() {
        List<MultipleChoiceQuestion> questions = new ArrayList<MultipleChoiceQuestion>();
        MultipleChoiceQuestion question1 = new MultipleChoiceQuestion("How long can you walk?");
        question1.addChoice("a mile", 100);
        question1.addChoice("two miles", 200);
        question1.addChoice("three years", 1000);
        question1.addChoice("I can't I am a carrot", -100);
        questions.add(question1);

        MultipleChoiceQuestion question2 = new MultipleChoiceQuestion("How smart are you?");
        question2.addChoice("smarter than a fifth grader", 100);
        question2.addChoice("who are you again?", -100);
        question2.addChoice("not smart", 0);
        questions.add(question2);
        return new Quiz("dumbid", "sillyTitle", questions, new ArrayList<Game>());
    }

    static Player generateRandomPlayer() {
        Player p = new Player(UUID.randomUUID().toString());
        return p;
    }

}