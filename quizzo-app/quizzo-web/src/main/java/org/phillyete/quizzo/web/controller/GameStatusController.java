package org.phillyete.quizzo.web.controller;


import javax.servlet.http.HttpSession;

import org.phillyete.quizzo.web.controller.gamestatus.*;
import org.phillyete.quizzo.web.engine.GameRunEngine;
import org.phillyete.quizzo.web.engine.GameState;
import org.phillyete.quizzo.web.engine.PlayerGameSession;
import org.phillyete.quizzo.repository.PlayerAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /**
     * This code currently smells. Too much complexity. Must. Break. Down.
     * We want one payload - here is who you are, your status, your game title, and score...
     *
     * @param session
     * @return a subclass of GameStatus which indicates the situation
     */
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    GameStatus getGameStatus(HttpSession session) {

        GameStatus gameStatus = null;

        PlayerGameSession playerGameSession = getOrCreatePlayerGameSession(session);
        // do we have a real player or not? If we do, calculate that score...
        if (!playerGameSession.hasPlayerAndGame()) {
            gameStatus = new NoGameSessionCreated();
        } else if (!playerGameSession.hasGame()) {
            gameStatus = new NoGameSessionCreated();
        } else if (!gameRunEngine.gameExists(playerGameSession.getGameId())) {
            gameStatus = new GameNotFound();
        } else {


            GameState gameState = gameRunEngine.getGameState(playerGameSession.getGameId());

            // if we're in a game with a score, we need it...
            int score = getCurrentScore(playerGameSession);

            // adapt potentially finer-grained state list to set of GameStatus subclasses. Each status
            // will then be translated to JSON.
            gameStatus = decodeGameStatus(playerGameSession, score, gameState);

            gameStatus.setCurrentScore(score);
        }

        String gameId = playerGameSession.getGameId();
        // finally, salt the output with the player information and game information
        gameStatus.setGameId(gameId);
        // hate doing this. Feels wrong.
        gameStatus.setGameTitle(gameRunEngine.getTitleForGameId(gameId));

        gameStatus.setPlayerNickName(playerGameSession.getPlayerId());


        return gameStatus;
    }

    private int getCurrentScore(PlayerGameSession playerGameSession) {
        int score = 0;
        try {
            // try to fetch score from Mongo
            score = playerAnswerRepository.calculateScore(
                    playerGameSession.getGameId(),
                    playerGameSession.getPlayerId());

        } catch (RuntimeException re) {
            re.printStackTrace();
            // kludge for now, should do more than return 0
        }
        return score;
    }


    private GameStatus decodeGameStatus(PlayerGameSession playerGameSession, int score, GameState gameState) {
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
                gameStatus = new GameComplete(score);
                break;
            default:
                // TODO - there are hills yet to climb, mountains yet to scramble
                // alas, they aren't gonna be scaled by this exception...
                // further cases hiding in here...
                gameStatus = new InvalidGameStatus();
        }

        return gameStatus;
    }
}
