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
package org.phillyete.quizzo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

/**
 * @author David Turanski
 *
 */
@Configuration
@EnableMongoRepositories(
        basePackages="org.phillyete.quizzo.repository",
        repositoryImplementationPostfix = "CustomImpl")
public class DataAccessConfig extends AbstractMongoConfiguration {
	private String hostName="localhost";
	@Override
	protected String getDatabaseName() {
		return "quizzo";
	}

	@Override
	public Mongo mongo() throws Exception {
		Mongo mongo = new Mongo(hostName);
		mongo.setWriteConcern(WriteConcern.SAFE);
		return mongo;
	}

}
