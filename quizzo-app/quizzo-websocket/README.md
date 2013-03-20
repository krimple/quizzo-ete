Quizzo-websocket (WebSocket Demo)
==========================================
### Overview

A Demo using WebSockets and [Spring Integration Websocket extensions for TCP adapters](https://github.com/SpringSource/spring-integration-extensions/tree/master/spring-integration-ip-extensions) 


### Instructions

Compile and run WebSocketServer with command line args <spring-profile> <game-id>

with no args, will generate random player answers ('test' profile)

The 'test-mongo' profile uses mongo to store and retrieve randomly generated player answers

the 'default' profile requires a real game id (UUID), typically the current game in progress.


Run the program. This will start listening on port 8081. 

open your browser to file:///.../quizzo-websocket/src/main/webapp/index.html

Hit <Enter> to start polling for data. 

*Note, you can chart answers submitted for a complete game or a game in progress*

If monitoring a game in progress. You need to provide the game id (instance id) of the current game.

Hit <Enter> to stop the server


 