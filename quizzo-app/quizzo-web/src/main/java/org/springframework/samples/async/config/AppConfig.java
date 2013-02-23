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
package org.springframework.samples.async.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.data.examples.quizzo.config.DataAccessConfig;
import org.springframework.data.examples.quizzo.config.ServiceConfig;
import org.springframework.data.examples.quizzo.repository.PlayerAnswerRepository;
import org.springframework.data.examples.quizzo.repository.PlayerRepository;
import org.springframework.data.examples.quizzo.repository.QuizRepository;
import org.springframework.samples.async.quizzo.engine.inmemory.GameRunEngineInMemoryImpl;
import org.springframework.samples.async.quizzo.hideme.GameRunner;
import org.springframework.samples.async.quizzo.engine.PlayerGameSession;
import org.springframework.samples.async.quizzo.engine.GameRunEngine;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author David Turanski
 *
 */
@Configuration
@Import({ ServiceConfig.class, DataAccessConfig.class })
public class AppConfig {

     @Bean
     @Scope(value = WebApplicationContext.SCOPE_SESSION)
     public PlayerGameSession playerGameSession() {
         return new PlayerGameSession();
     }

	 @Autowired
	 QuizRepository quizRepository;
	 @Autowired
	 PlayerAnswerRepository playerAnswerRepository;
	 @Autowired
	 PlayerRepository playerRepository;
	 
	 @Bean
	 public GameRunner gameRunner() {
		 return new GameRunner(quizRepository,playerAnswerRepository,playerRepository);
	 }

     @Bean
     public GameRunEngine quizRunEngine() {
         return new GameRunEngineInMemoryImpl(quizRepository, playerRepository,  playerAnswerRepository);
     }

}
