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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

/**
 * @author David Turanski
 *
 */
public class MultipleChoiceQuestionTest {
	@Test
	public void testQuestion() {
		MultipleChoiceQuestion q = new MultipleChoiceQuestion("What is the best programming language?");
		q.addChoice("Java", 5);
		q.addChoice("Javascript", 10);
		q.addChoice("Groovy",20);
		q.addChoice("Cobol",0);
		
		 
		assertEquals("What is the best programming language?",q.getText());
		
		assertEquals(4, q.getChoices().size());
		assertNotNull(q.getChoice(Choice.Letter.a));
		assertNotNull(q.getChoice(Choice.Letter.b));
		assertEquals(5,q.getChoice('a').getScore());
		assertEquals("Groovy",q.getChoice(Choice.Letter.c).getText());
		assertEquals("Cobol",q.getChoice(Choice.Letter.d).getText());
		assertNull(q.getChoice(Choice.Letter.e));
		 
	}
}
