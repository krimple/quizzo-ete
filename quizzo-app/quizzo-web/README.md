Quizzo-web
==========================================
### Overview

Web layer for a sample Quizzo app using Spring MVC 3.2, Servlet-based, async request processing. This uses MongoDB as a data store. Mongo must be installed separately and running. 

### Note

There is a bug in Tomcat that affects this sample. Please, use Tomcat 7.0.32 or higher.

### Instructions

Eclipse users run `mvn eclipse:eclipse` and then import the project. Or just import the code as a Maven project into IntelliJ, NetBeans, or Eclipse.

     mvn tomcat7:run
 
Or run from a compatible servlet container from your IDE


### Usage

_NOTE: curently maven configures the content path to 'quizzo'(preferred), IDE still uses'quizzo-web'_

* Load a quiz to Mongo from a file - Run QuizLoader.groovy {fileName} (in the data-access module). The project includes JavascriptQuiz.txt for testing

* Single question query - 
 		
 		GET http://localhost:8080/quizzo-web/quiz/question/{quizId}/{questionNumber}
 	
 	e.g.,


       http://localhost:8080/quizzo-web/quiz/question/JavascriptQuiz/5


* Start a new game        
        POST http://localhost:8080/quizzo-web/quiz/start/{quizName}
e.g., 
        
        curl -X POST http://localhost:8080/quizzo-web/quiz/start/JavascriptQuiz

* Get the next question

		GET http://localhost:8080/quizzo-web/quiz/nextq

A nextq request should be sent when the client loads, so the app is either waiting for the game to start or will join a game in progress

* Post a response
 
        POST http://localhost:8080/quizzo-web/quiz/{questionNumber}/{choiceLetter}
e.g., 

		curl -H "Content-Type: application/json" -X POST -d 		'{"playerId":"dave","questionNumber":"1","choice":"a"}' http://localhost:		8080/quizzo-web/quiz/answer
			

	

