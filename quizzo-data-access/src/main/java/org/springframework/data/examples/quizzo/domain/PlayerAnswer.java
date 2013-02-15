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

import org.springframework.util.Assert;

/**
 * @author David Turanski
 *
 */
public class PlayerAnswer {
	private final String playerId;
	private final String quizId;
	private final int questionNumber;
	private final String instanceId;
	private final Choice.Letter choice;
	
	public PlayerAnswer(String playerId, String instanceId, String quizId, int questionNumber, char choice) {
		Assert.hasLength(playerId,"player ID cannot be null or blank.");
		Assert.hasLength(instanceId,"instance ID cannot be null or blank.");
		Assert.hasLength(quizId,"quiz ID cannot be null or blank.");
		Assert.isTrue(questionNumber >= 0,"question number must be >= 0.");
		
		this.playerId = playerId;
		this.instanceId = instanceId;
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
	 * @return the instanceId
	 */
	public String getInstanceId() {
		return instanceId;
	}
	/**
	 * @return the choice
	 */
	public String getChoice() {
		return choice.name();
	}
	
}
