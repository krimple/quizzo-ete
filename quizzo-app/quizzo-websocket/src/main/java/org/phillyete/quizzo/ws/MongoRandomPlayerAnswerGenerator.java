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

/**
 * @author David Turanski
 *
 */
@Component("playerAnswerService")
@Profile("test-mongo")
public class MongoRandomPlayerAnswerGenerator extends RandomPlayerAnswerGenerator {

	private final PlayerAnswerRepository playerAnswerRepository;
	private List<PlayerAnswer> playerAnswers;

	@Autowired
	public MongoRandomPlayerAnswerGenerator(PlayerAnswerRepository playerAnswerRepository) {
		super();
		this.playerAnswerRepository = playerAnswerRepository;
		playerAnswers = new ArrayList<PlayerAnswer>();
		List<PlayerAnswer> answers = playerAnswerRepository.findByGameId(getGameId());
		playerAnswerRepository.delete(answers);
	}

	/* (non-Javadoc)
	 * @see quizzo.ete.PlayerAnswerService#getNewAnswers()
	 */
	@Override
	public List<PlayerAnswer> getNewAnswers() {
		List<PlayerAnswer> answers = playerAnswerRepository.findByGameId(getGameId());
		List<PlayerAnswer> delta = new ArrayList<PlayerAnswer>();
		for (PlayerAnswer answer: answers){
			if (!playerAnswers.contains(answer)) {
				delta.add(answer);
				playerAnswers.add(answer);
			}
		}
		return delta;
		 
	}

	@Override
	protected void savePlayerAnswer(PlayerAnswer playerAnswer) {
		playerAnswerRepository.save(playerAnswer);
	}
	
	


}
