package org.springframework.data.examples.quizzo;

import org.springframework.data.examples.quizzo.domain.Player;
import org.springframework.data.examples.quizzo.exception.PlayerAlreadyExistsException;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/21/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public interface PlayerService {
    Player registerPlayer(String playerId) throws PlayerAlreadyExistsException;

    int countPlayersByNamePattern(String pattern);

    Player fetchUniquePlayerByName(String name);
}
