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
	private String quizId;
	private final int questionNumber;
	private String gameId;
	private final char choice;
    private final int score;

    /**
	 * Constructor for submitted responses
	 * @param playerId
	 * @param questionNumber
	 * @param choice
	 */
	@JsonCreator
	public PlayerAnswer(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("questionNumber") Integer questionNumber,
			@JsonProperty("choice") char choice,
            @JsonProperty("score") Integer score) {
		this(playerId, null, null, questionNumber, choice, score);
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
}
