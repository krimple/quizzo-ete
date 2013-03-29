package org.phillyete.quizzo.web.controller;

import org.phillyete.quizzo.web.controller.moderator.GameNotStartedResponse;
import org.phillyete.quizzo.web.controller.moderator.GameStartedResponse;
import org.phillyete.quizzo.web.controller.moderator.OkModeratorResponse;
import org.phillyete.quizzo.web.controller.moderator.QuizModeratorResponse;
import org.phillyete.quizzo.web.engine.GameRunEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/game/")
public class QuizModeratorController extends AbstractQuizController {

    private GameRunEngine gameRunEngine;

    @Autowired
    public QuizModeratorController(GameRunEngine gameRunEngine) {
        this.gameRunEngine = gameRunEngine;

    }

    @RequestMapping(method = RequestMethod.POST, value = "{quizId}/startGame",
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
            produces = "application/json")
    public
    @ResponseBody
    QuizModeratorResponse beginPlay(@PathVariable String gameId) {
        gameRunEngine.stopTakingPlayersAndStartGamePlay(gameId);
        return new OkModeratorResponse();

    }

    @RequestMapping(method = RequestMethod.POST, value = "{gameId}/nextQuestion",
            produces = "application/json")
    public
    @ResponseBody
    QuizModeratorResponse nextQuestion(@PathVariable String gameId) {
        gameRunEngine.moveToNextQuestion(gameId);
        return new OkModeratorResponse();
    }

    @RequestMapping(method = RequestMethod.POST, value = "{gameId}/endQuestion",
            produces = "application/json")
    public
    @ResponseBody
    QuizModeratorResponse endQuestion(@PathVariable String gameId) {
        gameRunEngine.endQuestion(gameId);
        return new OkModeratorResponse();
    }

    @RequestMapping(method = RequestMethod.POST, value = "{gameId}/endGame",
            produces = "application/json")
    public
    @ResponseBody
    QuizModeratorResponse endGame(@PathVariable String gameId) {
        gameRunEngine.endGame(gameId);
        return new OkModeratorResponse();
    }

    @RequestMapping(method = RequestMethod.POST, value = "{gameId}/destroyGame",
            produces = "application/json")
    public
    @ResponseBody
    QuizModeratorResponse destroyGame(@PathVariable String gameId) {
        gameRunEngine.destroyQuizRun(gameId);
        return new OkModeratorResponse();
    }
}