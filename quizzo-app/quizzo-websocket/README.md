Quizzo-websocket (WebSocket Demo)
==========================================
### Overview

A Demo using WebSockets and [Spring Integration Websocket extensions for TCP adapters](https://github.com/SpringSource/spring-integration-extensions/tree/master/spring-integration-ip-extensions). 

WebSocketServer.java starts a Spring Integration application with a tcp inbound adapter configured with a server socket listening on port 8081. The inbound adapter is configured with an interceptor to handle the WS handshake. Once the connection is made, the server will start polling for player answers using the PlayerAnswerService. There are a few implementations of this, each enabled via a corresponding Spring profile:   

 * The 'test' profile generates random answers for each question with random delays. Not entirely random, the responses are weighted to produce "correct" response at least 80% of the time. 
  
 * The 'test-mongo' profile uses Mongo to store and retrieve randomly generated player answers

 * the 'default' profile polls real data in Mongo, so requires an actually game id (UUID), typically the Id of the current game in progress.


### Instructions

Compile and run WebSocketServer with command line args <spring-profile> <game-id>

	> cd quizo-ete/quizzo-app
	> mvn install
	> cd quizzo-websocket
	> mvn mvn exec:java -Dspring.profile=test -DgameId=game 


with no args, will generate random player answers ('test' profile)

* Run the program. This will start a server socket on port 8081. 

* Open your browser to file:///.../quizzo-websocket/src/main/webapp/index.html

* Hit Enter to start polling for data. 

* Hit Enter to stop the server

*Note, you can also chart answers submitted for a complete game but be sure to open the web app before you start polling since all the answers will be returned on the first poll, and none on subsequent polls, since all the duplicates will be removed*


 