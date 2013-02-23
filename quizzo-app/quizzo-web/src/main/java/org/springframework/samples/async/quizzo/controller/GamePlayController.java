package org.springframework.samples.async.quizzo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.PlayerAnswer;
import org.springframework.samples.async.quizzo.engine.PlayerGameSession;
import org.springframework.samples.async.quizzo.engine.inmemory.GameRunEngineInMemoryImpl;
import org.springframework.samples.async.quizzo.responses.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/quizRun")
public class GamePlayController extends AbstractQuizController {

    /* this is session-scoped */
    private PlayerGameSession playerGameSession;
    private GameRunEngineInMemoryImpl quizRunEngine;


    @Autowired
    public GamePlayController(PlayerGameSession playerGameSession,
                              GameRunEngineInMemoryImpl quizRunEngine) {
        this.playerGameSession = playerGameSession;
        this.quizRunEngine = quizRunEngine;
    }

    @RequestMapping(method = RequestMethod.POST, value = "game/{id}/joinGame")
    public @ResponseBody QuizPollResponse joinGame(@PathVariable String gameId) {
        String playerId = playerGameSession.getPlayerId();
        Assert.notNull(playerId);
        Assert.isTrue(quizRunEngine.gameExists(gameId));

        quizRunEngine.addPlayer(gameId, playerId);
        return new GameJoinedResponse();
    }


    @RequestMapping(method = RequestMethod.GET, value = "currentQuestion")
    public @ResponseBody QuizPollResponse getCurrentQuestion(HttpServletResponse response) {
        String gameId = playerGameSession.getGameId();
        MultipleChoiceQuestion question = quizRunEngine.getCurrentQuizQuestion(gameId);
        if (question != null) {
            return new QuestionPendingResponse(question);
        } else {
            // todo - need more fine level of error responses - what if we're done?
            return new AwaitingNextQuestionResponse();
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "submitAnswer")
    public @ResponseBody QuizPollResponse submitPlayerAnswer(@RequestBody PlayerAnswer playerAnswer) {
        char choice = playerAnswer.getChoice();
        int questionNumber = playerAnswer.getQuestionNumber();
        return new AnswerSubmittedResponse();
    }

}
