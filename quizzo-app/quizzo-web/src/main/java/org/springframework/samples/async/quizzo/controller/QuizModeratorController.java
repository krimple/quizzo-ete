package org.springframework.samples.async.quizzo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.async.quizzo.controller.moderator.*;
import org.springframework.samples.async.quizzo.engine.GameRunEngine;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/game/")
public class QuizModeratorController extends AbstractQuizController {

    private GameRunEngine gameRunEngine;

    @Autowired
    public QuizModeratorController(GameRunEngine gameRunEngine) {
        this.gameRunEngine = gameRunEngine;

    }

    @RequestMapping(method = RequestMethod.POST, value = "{quizId}/startGame",
            consumes = "application/json",
            produces = "application/json")
    public
    @ResponseBody
    QuizModeratorResponse startQuiz(@PathVariable String quizId,
                                    @RequestParam(value = "title", required = false) String title) {
        String gameId =
                gameRunEngine.startQuizRunAndBeginTakingPlayers(quizId, title);
        if (gameId != null) {
            return new GameStartedResponse(gameId);
        } else {
            return new GameNotStartedResponse();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "{gameId}/beginPlay",
            consumes = "application/json",
            produces = "application/json")
    public
    @ResponseBody
    QuizModeratorResponse beginPlay(@PathVariable String gameId) {
        gameRunEngine.stopTakingPlayersAndStartGamePlay(gameId);
        return new OkModeratorResponse();

    }

    @RequestMapping(method = RequestMethod.POST, value = "{gameId}/nextQuestion",
            consumes = "application/json",
            produces = "application/json")
    public
    @ResponseBody
    QuizModeratorResponse nextQuestion(@PathVariable String gameId) {
        gameRunEngine.moveToNextQuestion(gameId);
        return new OkModeratorResponse();
    }

    @RequestMapping(method = RequestMethod.POST, value = "{gameId}/endQuestion",
            consumes = "application/json",
            produces = "application/json")
    public
    @ResponseBody
    QuizModeratorResponse endQuestion(@PathVariable String gameId) {
        gameRunEngine.endQuestion(gameId);
        return new OkModeratorResponse();
    }

    @RequestMapping(method = RequestMethod.POST, value = "{gameId}/endGame",
            consumes = "application/json",
            produces = "application/json")
    public
    @ResponseBody
    QuizModeratorResponse endGame(@PathVariable String gameId) {
        gameRunEngine.endGame(gameId);
        return new OkModeratorResponse();
    }

    @RequestMapping(method = RequestMethod.POST, value = "{gameId}/destroyGame",
            consumes = "application/json",
            produces = "application/json")
    public
    @ResponseBody
    QuizModeratorResponse destroyGame(@PathVariable String gameId) {
        gameRunEngine.destroyQuizRun(gameId);
        return new OkModeratorResponse();
    }
}