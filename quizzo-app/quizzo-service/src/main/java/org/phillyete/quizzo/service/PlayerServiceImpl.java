package org.phillyete.quizzo.service;

import org.phillyete.quizzo.domain.Player;
import org.phillyete.quizzo.exception.PlayerAlreadyExistsException;
import org.phillyete.quizzo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository repository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Player registerPlayer(String playerId) throws PlayerAlreadyExistsException {
        Assert.hasText(playerId, "player ID cannot be null or blank.");
        if (repository.findOne(playerId) != null) {
            throw new PlayerAlreadyExistsException();
        }

        Player player = new Player(playerId);
        return repository.save(player);
    }

    @Override
    public int countPlayersByNamePattern(String pattern) {
        return repository.findByNameLike(pattern).size();
    }

    @Override
    public Player fetchUniquePlayerByName(String name) {
        return repository.findOneByName(name);
    }
}
