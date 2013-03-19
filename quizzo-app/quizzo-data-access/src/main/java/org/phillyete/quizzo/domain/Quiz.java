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
package org.phillyete.quizzo.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.util.Assert;

/**
 * @author David Turanski
 *
 */
public class Quiz {
	private final String id;
	private final String title;
	private final List<MultipleChoiceQuestion> questions;
	private final List<Game> gamesPlayed;

	@PersistenceConstructor
	public Quiz(String id, String title, List<MultipleChoiceQuestion> questions, List<Game> gamesPlayed) {
		Assert.notNull(questions, "questions list cannot be null");
		Assert.notNull(id, "id cannot be null or blank");
		this.id = id;
		this.title = title;
		Collections.sort(questions, new Comparator<MultipleChoiceQuestion>() {
			@Override
			public int compare(MultipleChoiceQuestion q1, MultipleChoiceQuestion q2) {
				return q1.getQuestionNumber() - q2.getQuestionNumber();
			}
		});
		this.questions  = questions;
		this.gamesPlayed = gamesPlayed;
	}
	
	public Quiz(String id, String title, List<MultipleChoiceQuestion> questions) {
		this(id,title,questions,new ArrayList<Game>());
	}

	public Quiz(String id, String title) {
		this(id, title, new ArrayList<MultipleChoiceQuestion>(), new ArrayList<Game>());
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the questions
	 */
	public List<MultipleChoiceQuestion> getQuestions() {
		return questions;
	}

	/**
	 * @return the instances
	 */
	public List<Game> getGamesPlayed() {
		return gamesPlayed;
	}

	public void addQuestion(MultipleChoiceQuestion question) {
		Assert.notNull(question, "question cannot be null");
		if (question.getQuestionNumber() == 0) {
			question.setQuestionNumber(questions.size() + 1);
		}
		this.questions.add(question);
	}

	public Game startGame(String title) {
		Game game = new Game(title);
		this.gamesPlayed.add(game);
		return game;
	}

	public int getScore(PlayerAnswer answer) {
		return questions.get(answer.getQuestionNumber()-1).getChoice(answer.getChoice()).getScore();
	}

}
