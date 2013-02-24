package org.springframework.samples.async.quizzo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.PlayerAnswer;
import org.springframework.samples.async.quizzo.engine.GameRunEngine;
import org.springframework.samples.async.quizzo.engine.PlayerGameSession;
import org.springframework.samples.async.quizzo.engine.PlayerGameSessionImpl;
import org.springframework.samples.async.quizzo.responses.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/quizRun")
public class GamePlayController extends AbstractQuizController {

    private GameRunEngine quizRunEngine;


    @Autowired
    public GamePlayController(GameRunEngine quizRunEngine) {
        this.quizRunEngine = quizRunEngine;
    }

    @RequestMapping(method = RequestMethod.POST, value = "game/{gameId}/joinGame")
    public @ResponseBody QuizPollResponse joinGame(HttpSession session, @PathVariable String gameId) {
        PlayerGameSession playerGameSession = getOrCreatePlayerGameSession(session);
        String playerId = playerGameSession.getPlayerId();
        Assert.notNull(playerId);
        Assert.isTrue(quizRunEngine.gameExists(gameId));
        quizRunEngine.addPlayer(gameId, playerId);
        playerGameSession.setGameId(gameId);
        return new GameJoinedResponse();
    }


    @RequestMapping(method = RequestMethod.GET, value = "currentQuestion")
    public @ResponseBody QuizPollResponse getCurrentQuestion(
            HttpSession session, HttpServletResponse response) {
        String gameId = getOrCreatePlayerGameSession(session).getGameId();
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
