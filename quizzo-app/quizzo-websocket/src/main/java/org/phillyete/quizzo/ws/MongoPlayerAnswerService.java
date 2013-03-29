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
package org.phillyete.quizzo.ws;

import java.util.ArrayList;
import java.util.List;

import org.phillyete.quizzo.domain.PlayerAnswer;
import org.phillyete.quizzo.repository.PlayerAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author David Turanski
 *
 */
@Component("playerAnswerService")
@Profile("default")
public class MongoPlayerAnswerService implements PlayerAnswerService {

	private PlayerAnswerRepository playerAnswerRepository;
	private ArrayList<PlayerAnswer> playerAnswers;
	private String getGameId;
	
	@Autowired
	public MongoPlayerAnswerService(PlayerAnswerRepository playerAnswerRepository) {
		this.playerAnswerRepository = playerAnswerRepository;
		this.playerAnswers = new ArrayList<PlayerAnswer>();
	}
	
	/* (non-Javadoc)
	 * @see org.phillyete.quizzo.ws.PlayerAnswerService#getNewAnswers()
	 */
	@Override
	public List<PlayerAnswer> getNewAnswers() {
		Assert.notNull(this.getGameId,"gameId cannot be null");
		
		List<PlayerAnswer> delta = new ArrayList<PlayerAnswer>();
		List<PlayerAnswer> answers = playerAnswerRepository.findByGameId(getGameId());
		for (PlayerAnswer answer: answers){
			if (!playerAnswers.contains(answer)) {
				delta.add(answer);
				playerAnswers.add(answer);
			}
		}
		return delta;
	}

	/**
	 * @return
	 */
	public String getGameId() {
		return this.getGameId;
	}
	
	public void setGameId(String gameId) {
		this.getGameId = gameId;
	}
}
