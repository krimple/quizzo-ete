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

import java.util.UUID;

import org.joda.time.DateTime;

/**
 * @author David Turanski
 *
 */
public class Game {
	private final String id;
	private final DateTime startTime;
	
	public Game() {
		id = UUID.randomUUID().toString();
		startTime = new DateTime();
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
 
	/**
	 * @return the startTime
	 */
	public DateTime getStartTime() {
		return startTime;
	}
}