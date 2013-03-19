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
package org.phillyete.quizzo;

import org.phillyete.quizzo.domain.PlayerAnswer;
import org.springframework.util.Assert;

/**
 * @author David Turanski
 *
 */
public class ScoreSummary {
	private final String playerId;
	private final String gameId;
	private int score;
	
	public ScoreSummary(PlayerAnswer answer) {
		this(answer.getPlayerId(), answer.getGameId());
	}
	
	public ScoreSummary(String playerId,String gameId) {
		Assert.hasText(playerId,"playerId cannot be null or blank.");
		Assert.hasText(gameId, "gameId cannot be null or blank.");
		this.playerId = playerId;
		this.gameId = gameId;
		score = 0;
	}
	
	/**
	 * @return the playerId
	 */
	public String getPlayerId() {
		return playerId;
	}
	 
	/**
	 * @return the gameId
	 */
	public String getGameId() {
		return gameId;
	}
	
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	public int incrementScoreBy(int score) {
		this.score += score;
		return this.score;
	}
}
