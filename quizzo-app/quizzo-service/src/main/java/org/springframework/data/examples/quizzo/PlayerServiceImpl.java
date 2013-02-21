package org.springframework.data.examples.quizzo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.Player;
import org.springframework.data.examples.quizzo.exception.PlayerAlreadyExistsException;
import org.springframework.data.examples.quizzo.repository.PlayerRepository;
import org.springframework.util.Assert;

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
