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
package org.springframework.data.examples.quizzo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.examples.quizzo.PlayerService;
import org.springframework.data.examples.quizzo.PlayerServiceImpl;
import org.springframework.data.examples.quizzo.ScoreService;
import org.springframework.data.examples.quizzo.repository.PlayerAnswerRepository;
import org.springframework.data.examples.quizzo.repository.PlayerRepository;
import org.springframework.data.examples.quizzo.repository.QuizRepository;

/**
 * @author David Turanski
 *
 */
@Configuration
@Import(DataAccessConfig.class)
public class ServiceConfig {
	 @Autowired
	 QuizRepository quizRepository;
	 @Autowired
	 PlayerAnswerRepository playerAnswerRepository;
	 @Autowired
	 PlayerRepository playerRepository;
	 
	 @Bean
	 ScoreService scoreService() {
		 return new ScoreService(playerAnswerRepository, quizRepository);
	 }
	 
	 @Bean
     PlayerService playerService() {
		 return new PlayerServiceImpl(playerRepository);
	 }
}
