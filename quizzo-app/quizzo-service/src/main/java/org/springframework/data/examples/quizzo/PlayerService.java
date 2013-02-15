/*
 * Copyright 2002-2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.data.examples.quizzo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.Player;
import org.springframework.data.examples.quizzo.exception.PlayerAlreadyExistsException;
import org.springframework.data.examples.quizzo.repository.PlayerRepository;
import org.springframework.util.Assert;

/**
 * @author David Turanski
 *
 */
public class PlayerService {
	@Autowired
	private final PlayerRepository repository;

	public PlayerService(PlayerRepository repository) {
		this.repository = repository;
	}

	public Player registerPlayer(String playerId) throws PlayerAlreadyExistsException {
		Assert.hasText(playerId,"player ID cannot be null or blank.");
		if (repository.findOne(playerId) != null) {
			throw new PlayerAlreadyExistsException();
		}

		Player player = new Player(playerId);
		return repository.save(player);
	}
}
