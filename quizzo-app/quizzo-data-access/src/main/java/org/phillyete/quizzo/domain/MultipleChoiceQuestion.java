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

import java.util.HashMap;
import java.util.Map;

import org.phillyete.quizzo.domain.Choice.Letter;
import org.springframework.util.Assert;

/**
 * @author David Turanski
 *
 */
public class MultipleChoiceQuestion {
	private final String text;
	private int questionNumber;
	private final Map<Choice.Letter,Choice> choices;
	 
	public MultipleChoiceQuestion(String text) {
		Assert.hasText(text,"text cannot be null or blank");
	 
		
		this.text = text;
		this.choices = new HashMap<Choice.Letter,Choice>();
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @return the answers
	 */
	public Map<Choice.Letter,Choice> getChoices() {
		return choices;
	}
	
	public void addChoice(String text,int score) {
		Assert.isTrue(choices.size() < Choice.Letter.values().length,
				"maxium number of choices has been added");
		Letter letter=Letter.values()[choices.size()];
		choices.put(letter,new Choice(text,letter,score));
	}
	
	public Choice getChoice(Choice.Letter letter) {
		return choices.get(letter);
	}
	
	public Choice getChoice(char letter) {
		Letter ltr = Choice.Letter.fromChar(letter);
		return ltr == null? null: getChoice(ltr);
	}
	/**
	 * @return the questionNumber
	 */
	public int getQuestionNumber() {
		return questionNumber;
	}
	/**
	 * @param questionNumber the questionNumber to set
	 */
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
}
