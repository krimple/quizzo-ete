package org.springframework.data.examples.quizzo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.repository.QuizRepository;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/22/13
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuizServiceImpl implements QuizService {

    private QuizRepository quizRepository;

    @Autowired
    public QuizServiceImpl(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Override
    public boolean quizExists(String title) {
        return quizRepository.findOneByTitle(title) != null;
    }
}
