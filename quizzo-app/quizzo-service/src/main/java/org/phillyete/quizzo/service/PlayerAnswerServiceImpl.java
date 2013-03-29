package org.phillyete.quizzo.service;

import org.phillyete.quizzo.domain.PlayerAnswer;
import org.phillyete.quizzo.repository.PlayerAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/27/13
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
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
