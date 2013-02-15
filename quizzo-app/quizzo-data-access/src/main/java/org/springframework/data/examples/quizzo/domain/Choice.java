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

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author David Turanski
 *
 */
public class Choice {
	private final String text;
	private final Letter letter;
	private final int score;

	public static enum Letter {
		a, b, c, d, e;
		public static Letter fromChar(char letter) {
			Letter ltr = null;
			try {
				ltr = Letter.valueOf(String.valueOf(letter));
			} catch (Exception e) {
				
			}
			return ltr;
		}
		public static Letter validate(char letter) {
			Letter ltr = null;
			 
			ltr = Letter.fromChar(letter);
			if (ltr==null) {
				throw new RuntimeException("letter must be one of "
						+ StringUtils.arrayToCommaDelimitedString(Letter.values()));
			}
			return ltr;
		}
	}

	public Choice(String text, char letter, int score) {
		Assert.hasText(text, "text cannot be null or blank");
		this.letter = Letter.validate(letter);
		this.text = text;
		this.score = score;
	}

	@PersistenceConstructor
	public Choice(String text, Letter letter, int score) {
		Assert.hasText(text, "text cannot be null or blank");
		this.letter = letter;
		this.text = text;
		this.score = score;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param letter the letter to set
	 */
	public char getLetter() {
		return letter.name().charAt(0);
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	
}
