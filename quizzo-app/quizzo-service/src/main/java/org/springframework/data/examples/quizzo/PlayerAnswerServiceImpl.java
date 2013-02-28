package org.springframework.data.examples.quizzo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.PlayerAnswer;
import org.springframework.data.examples.quizzo.repository.PlayerAnswerRepository;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/27/13
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerAnswerServiceImpl implements PlayerAnswerService {

    PlayerAnswerRepository playerAnswerRepository;

    @Autowired
    public PlayerAnswerServiceImpl(PlayerAnswerRepository playerAnswerRepository) {
        this.playerAnswerRepository = playerAnswerRepository;
    }

    @Override
    public void submitAnswer(PlayerAnswer playerAnswer) {
        // todo - check for uniqueness and enforce non-unique answer
        // probably by indexing uniquely against player id, quiz id, game id and question


    }
}
