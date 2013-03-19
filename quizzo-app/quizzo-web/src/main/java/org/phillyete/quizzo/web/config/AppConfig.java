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
package org.phillyete.quizzo.web.config;

import org.phillyete.quizzo.config.DataAccessConfig;
import org.phillyete.quizzo.config.ServiceConfig;
import org.phillyete.quizzo.engine.GameRunEngine;
import org.phillyete.quizzo.engine.PlayerGameSession;
import org.phillyete.quizzo.engine.PlayerGameSessionImpl;
import org.phillyete.quizzo.engine.inmemory.GameRunEngineInMemoryImpl;
import org.phillyete.quizzo.repository.PlayerAnswerRepository;
import org.phillyete.quizzo.repository.PlayerRepository;
import org.phillyete.quizzo.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
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
         return new PlayerGameSessionImpl();
     }

	 @Autowired
	 QuizRepository quizRepository;
	 @Autowired
	 PlayerAnswerRepository playerAnswerRepository;
	 @Autowired
	 PlayerRepository playerRepository;
	 
	 @Bean
	    public GameRunEngine gameRunEngine() {	
	        return new GameRunEngineInMemoryImpl(quizRepository, playerRepository, playerAnswerRepository);
	    }

}
