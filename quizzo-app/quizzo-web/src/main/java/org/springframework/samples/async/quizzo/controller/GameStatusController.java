package org.springframework.samples.async.quizzo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.repository.PlayerAnswerRepository;
import org.springframework.http.HttpMethod;
import org.springframework.samples.async.quizzo.controller.gamestatus.*;
import org.springframework.samples.async.quizzo.engine.GameRunEngine;
import org.springframework.samples.async.quizzo.engine.GameState;
import org.springframework.samples.async.quizzo.engine.PlayerGameSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "status")
public class GameStatusController extends AbstractQuizController {

    private PlayerAnswerRepository playerAnswerRepository;

    @Autowired
    public void setPlayerAnswerRepository(PlayerAnswerRepository playerAnswerRepository) {
        this.playerAnswerRepository = playerAnswerRepository;
    }

    private GameRunEngine gameRunEngine;

    @Autowired
    public void setGameRunEngine(GameRunEngine gameRunEngine) {
        this.gameRunEngine = gameRunEngine;
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody GameStatus getGameStatus(HttpSession session) {
        PlayerGameSession playerGameSession = getOrCreatePlayerGameSession(session);
        int score = 0; // TODO - need to aggregate score entries
        GameState gameState = gameRunEngine.getGameState(playerGameSession.getGameId());

        // adapt potentially finer-grained state list to set of GameStatus subclasses. Each status
        // will then be translated to JSON.
        final GameStatus gameStatus;
        switch (gameState) {
            case AWAITING_PLAYERS:
                gameStatus = new WaitingToPlay("We are waiting for the players to join...");
                break;
            case WAIT_NEXT_QUESTION:
                // todo - get last question information from PlayerAnswer information
                gameStatus = new WaitingForNextQuestion(0, "Please wait for next message...");
                break;
            case AWAITING_ANSWER:
                gameStatus = new WaitingForAnswer(0,
                        gameRunEngine.getCurrentQuizQuestion(playerGameSession.getGameId()));
                // could use some cleanup here...
                break;
            case COMPLETE:
                gameStatus = new GameComplete(0);
                break;
            default:
                gameStatus = new InvalidGameStatus();
        }

        return gameStatus;
    }
}
