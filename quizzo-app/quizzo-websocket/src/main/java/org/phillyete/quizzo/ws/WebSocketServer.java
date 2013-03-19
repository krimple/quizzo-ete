package org.phillyete.quizzo.ws;
import java.io.IOException;

import org.phillyete.quizzo.config.DataAccessConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

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
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		 context.getEnvironment().addActiveProfile("test");
		 context.register(Config.class);
		 context.refresh();
		 context.registerShutdownHook();
		 System.out.println("Hit <Enter> to Quit");
		 try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		context.close();
	}
	
	@Configuration
	@Import(DataAccessConfig.class)
	@ImportResource("/META-INF/spring/spring-integration-context.xml")
	public static class Config {
		
	}
}
