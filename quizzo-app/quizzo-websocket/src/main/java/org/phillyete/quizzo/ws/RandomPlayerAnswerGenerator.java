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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.phillyete.quizzo.domain.PlayerAnswer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author David Turanski
 *
 */
@Component("playerAnswerService")
@Profile("test")
public class RandomPlayerAnswerGenerator implements PlayerAnswerService, Runnable {
	private static final int NUMBER_QUESTIONS = 10;
	private static final int NUMBER_PLAYERS = 100;

	private Random rand = new Random();
	private List<String> players = new ArrayList<String>();
	private int questionNumber;
	private long id;
	private List<PlayerAnswer> playerAnswers;
	private boolean done;

	public RandomPlayerAnswerGenerator() {
		questionNumber = 1;
		playerAnswers = new ArrayList<PlayerAnswer>();
		new Thread(this).start();
	}

	/* (non-Javadoc)
	 * @see quizzo.ete.PlayerAnswerService#getNewAnswers()
	 */
	@Override
	public List<PlayerAnswer> getNewAnswers() {
		List<PlayerAnswer> results = Collections.unmodifiableList(new ArrayList<PlayerAnswer>(playerAnswers));
		playerAnswers.clear();
		return results;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		String correctResponse = randomChoice(null);
		while (questionNumber <= NUMBER_QUESTIONS) {
			String player = randomPlayer();
			if (player == null) {
				questionNumber++;
				if (questionNumber > NUMBER_QUESTIONS) {
					break;
				}
				correctResponse = randomChoice(null);
				players.clear();
				player = randomPlayer();
			}
			PlayerAnswer answer = new PlayerAnswer(questionNumber, randomChoice(correctResponse));
			answer.setPlayerId(player);
			answer.setId(BigInteger.valueOf(id++));
			answer.setGameId("game");
			answer.setQuizId("quiz");
			answer.setScore(10); // Doesn't matter for this
			try {
				Thread.sleep(rand.nextInt(1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			playerAnswers.add(answer);
		}
		done = true;
	}

	public boolean isDone() {
		return done;
	}

	/**
	 * @param questionNumber
	 * @return
	 */
	private String randomPlayer() {
		if (players.size() == NUMBER_PLAYERS) {
			return null;
		}

		String player = "player" + rand.nextInt(NUMBER_PLAYERS);
		while (players.contains(player)) {
			player = "player" + rand.nextInt(NUMBER_PLAYERS);
		}
		players.add(player);
		return player;
	}

	private String randomChoice(String correctResponse) {
		String[] choices = new String[] { "a", "b", "c", "d" };
		if (correctResponse != null) {

			// correct response guaranteed 80 % of the time

			boolean shouldReturnCorrectResponse = rand.nextInt(10) < 8;
			if (shouldReturnCorrectResponse) {
				return correctResponse;
			}
		}
		return choices[rand.nextInt(4)];
	}

}
