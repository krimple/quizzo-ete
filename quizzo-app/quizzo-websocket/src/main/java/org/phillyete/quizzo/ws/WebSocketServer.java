package org.phillyete.quizzo.ws;

import java.io.IOException;

import org.phillyete.quizzo.config.DataAccessConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;

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

/**
 * @author David Turanski
 *
 */

public class WebSocketServer {
	public static void main(String... args) {
		String springProfile = "test";
		String gameId = "game";

		if (args.length > 0 && args.length != 2) {
			System.out.println("Usage: WebSocketServer <spring-profile>(default,test,test-mongo), <gameId>");
			System.exit(1);
		} else if (args.length == 2) {
			springProfile = args[0];
			gameId = args[1];
		}

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().addActiveProfile(springProfile);
		context.getEnvironment().addActiveProfile(springProfile);
		context.register(Config.class);
		context.refresh();

		PlayerAnswerService pas = context.getBean(PlayerAnswerService.class);
		pas.setGameId(gameId);

		SourcePollingChannelAdapter adapter = context.getBean("inboundPoller", SourcePollingChannelAdapter.class);
		context.registerShutdownHook();
		System.out.println("Hit <Enter> to Start polling for game [" + gameId + "] answers");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter.start();

		System.out.println("Hit <Enter> to Quit");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		context.close();
		System.exit(0);
	}

	@Configuration
	@Import(DataAccessConfig.class)
	@ImportResource("/META-INF/spring/spring-integration-context.xml")
	public static class Config {

	}
}
