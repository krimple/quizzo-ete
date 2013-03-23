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
package org.phillyete.quizzo.util

/**
 * @author David Turanski
 *
 */
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.phillyete.quizzo.domain.*
import org.phillyete.quizzo.repository.*
import org.phillyete.quizzo.config.*
import org.springframework.context.support.ClassPathXmlApplicationContext

class QuizLoader {
	static main(String... args) {
		
		if (args.length != 1) {
			println "usage Quizloader <fileName>"
			System.exit(1)
		}
		ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext-data-access.xml");
		def quizRepository = ctx.getBean(QuizRepository.class)
		
		def quizfile = new File(args[0])
		println "loading ${quizfile.name}..."
		def quizId = quizfile.name.replaceAll(/\.txt/,'')
		Quiz qz = quizRepository.findOne(quizId)
		if (qz)	quizRepository.delete(qz)
		
		
		
		println "quiz id is '${quizId}'"
		def title = null;
		def questions = []
		def question
		quizfile.eachLine{
			title = title?: it //first line
			def q = it =~ /^\s*(\d+)\.\s+(.*)$/
			def a = it =~/^\s*[a|b|c|d|e]\.\s*(.+)\|\s*(\d+)$/
			if (q) {
				println "question: ${q[0][1]}'${q[0][2]}'"
				question = new MultipleChoiceQuestion(q[0][2])
				question.setQuestionNumber(q[0][1] as int)
				questions << question
			}
			if (a) {
				println "answer: ${a[0][1]} ${a[0][2]}"
				question.addChoice(a[0][1],a[0][2] as int)
			}
			
		}
		println "title is '$title'"
		
		def quiz = new Quiz(quizId,title,questions )
		quizRepository.save(quiz)
	}
}
