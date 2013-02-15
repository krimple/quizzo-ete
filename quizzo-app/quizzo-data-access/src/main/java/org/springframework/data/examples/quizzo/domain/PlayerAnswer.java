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
package org.springframework.data.examples.quizzo.domain;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.util.Assert;

/**
 * @author David Turanski
 *
 */
public class PlayerAnswer {
	private BigInteger id;
	private final String playerId;
	private final String quizId;
	private final int questionNumber;
	private final String gameId;
	
	private final Choice.Letter choice;
	
	public PlayerAnswer(BigInteger id, String playerId, String gameId, String quizId, int questionNumber,@Value("#root.choice") char choice){
		this(playerId,gameId, quizId, questionNumber, choice);
		this.id = id;
	}
	@PersistenceConstructor
	public PlayerAnswer(String playerId, String gameId, String quizId, int questionNumber, @Value("#root.choice") char choice) {

		Assert.hasText(playerId, "player ID cannot be null or blank.");
		Assert.hasText(gameId, "game ID cannot be null or blank.");
		Assert.hasText(quizId, "quiz ID cannot be null or blank.");
		Assert.isTrue(questionNumber >= 0, "question number must be >= 0.");

		this.playerId = playerId;
		this.gameId = gameId;
		this.quizId = quizId;
		this.questionNumber = questionNumber;
		this.choice = Choice.Letter.validate(choice);
	}

	/**
	 * @return the playerId
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * @return the quizId
	 */
	public String getQuizId() {
		return quizId;
	}

	/**
	 * @return the questionNumber
	 */
	public int getQuestionNumber() {
		return questionNumber;
	}

	/**
	 * @return the gameId
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * @return the choice
	 */
	public char getChoice() {
		return choice.name().charAt(0);
	}
	
	public BigInteger getId() {
		return id;
	}

}
