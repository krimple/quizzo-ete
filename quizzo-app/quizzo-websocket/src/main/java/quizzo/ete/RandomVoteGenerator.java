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
package quizzo.ete;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.examples.quizzo.config.DataAccessConfig;
import org.springframework.data.examples.quizzo.domain.PlayerAnswer;
import org.springframework.data.examples.quizzo.repository.PlayerAnswerRepository;
import org.springframework.stereotype.Component;

/**
 * @author David Turanski
 *
 */
@Component("playerAnswerService")
public class RandomVoteGenerator implements PlayerAnswerService, Runnable {
	private Random rand = new Random();
	private List<String> players = new ArrayList<String>();
	private int questionNumber;
	private long id;
	private final PlayerAnswerRepository playerAnswerRepository;
	private List<PlayerAnswer> playerAnswers;
	private boolean done;

	@Autowired
	public RandomVoteGenerator(PlayerAnswerRepository playerAnswerRepository) {
		this.playerAnswerRepository = playerAnswerRepository;
		questionNumber = 1;
		playerAnswers = new ArrayList<PlayerAnswer>();
		new Thread(this).start();
	}

	public static void main(String... args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(DataAccessConfig.class);
		PlayerAnswerRepository paRepo = ctx.getBean(PlayerAnswerRepository.class);

		RandomVoteGenerator rvg = new RandomVoteGenerator(paRepo);
		

		while (!rvg.done) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<PlayerAnswer> answers = rvg.getNewAnswers();
			for (PlayerAnswer answer : answers) {
				System.out.println("q:" + answer.getQuestionNumber() + ":" + answer.getPlayerId() + ":"
						+ answer.getChoice());
			}
		}
	}

	/**
	 * @param questionNumber
	 * @return
	 */
	private String randomPlayer() {
		if (players.size() == 100) {
			return null;
		}

		String player = "player" + rand.nextInt(100);
		while (players.contains(player)) {
			player = "player" + rand.nextInt(100);
		}
		players.add(player);
		return player;
	}

	/**
	 * @return
	 */
	private String randomChoice() {
		String[] choices = new String[] { "a", "b", "c", "d" };
		return choices[rand.nextInt(4)];
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
		while (questionNumber <= 10) {
			String player = randomPlayer();
			if (player == null) {
				questionNumber++;
				players.clear();
				player = randomPlayer();
			}
			PlayerAnswer answer = new PlayerAnswer(questionNumber, randomChoice());
			answer.setPlayerId(player);
			answer.setId(BigInteger.valueOf(id++));
			answer.setGameId("game");
			answer.setQuizId("quiz");
			answer.setScore(10);
			try {
				Thread.sleep(rand.nextInt(1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			playerAnswers.add(answer);
		}
		done  = true;
	}
}
