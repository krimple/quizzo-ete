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

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.util.Assert;

/**
 * @author David Turanski
 *
 */

@CompoundIndexes({
        @CompoundIndex(name = "player_answer_key_idx",
                       def = "{'quizId' : 1, 'gameId' : 1, 'playerId' : 1, 'questionNumber' : 1}",
                       unique = true)
})
public class PlayerAnswer {
	private BigInteger id;
	private String playerId;
	private String quizId;
	private final int questionNumber;
	private String gameId;
	private char choice;
    private int score;

    /**
	 * Constructor for submitted responses
	 * @param questionNumber
	 * @param choice
	 */
	@JsonCreator
	public PlayerAnswer(
			@JsonProperty("questionNumber") Integer questionNumber,
			@JsonProperty("choice") char choice) {
		this(null, null, null, questionNumber, choice, null);
	}
	
	@PersistenceConstructor
	public PlayerAnswer(String playerId, String gameId, String quizId,
                        Integer questionNumber, char choice, Integer score) {

		Assert.hasText(playerId, "player ID cannot be null or blank.");
		Assert.isTrue(questionNumber >= 0, "question number must be >= 0.");

		this.playerId = playerId;
		this.gameId = gameId;
		this.quizId = quizId;
		this.questionNumber = questionNumber;
		this.choice = choice;
        this.score = score;
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
		return choice;
	}
	
	public BigInteger getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}
	/**
	 * @param quizId the quizId to set
	 */
	public void setQuizId(String quizId) {
		this.quizId = quizId;
	}
	/**
	 * @param gameId the gameId to set
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

    public int getScore() {
        return score;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setChoice(char choice) {
        this.choice = choice;
    }
}
