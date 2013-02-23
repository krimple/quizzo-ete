package org.springframework.samples.async.quizzo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.PlayerService;
import org.springframework.data.examples.quizzo.ScoreService;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.data.examples.quizzo.domain.PlayerAnswer;
import org.springframework.data.examples.quizzo.repository.QuizRepository;
import org.springframework.http.HttpStatus;
import org.springframework.samples.async.quizzo.engine.PlayerGameSession;
import org.springframework.samples.async.quizzo.engine.QuizRun;
import org.springframework.samples.async.quizzo.engine.QuizRunEngine;
import org.springframework.samples.async.quizzo.responses.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/quizRun")
public class GamePlayController extends AbstractQuizController {

    /* this is session-scoped */
    private PlayerGameSession playerGameSession;

    private QuizRunEngine quizRunEngine;
    private final PlayerService playerService;
    private final QuizRepository quizRepository;
    private final ScoreService scoreService;


    @Autowired
    public GamePlayController(PlayerGameSession playerGameSession,
                              QuizRunEngine quizRunEngine,
                              PlayerService playerService,
                              QuizRepository quizRepository,
                              ScoreService scoreService) {
        this.playerGameSession = playerGameSession;
        this.quizRunEngine = quizRunEngine;
        this.playerService = playerService;
        this.quizRepository = quizRepository;
        this.scoreService = scoreService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "game/{id}/joinGame")
    public @ResponseBody QuizPollResponse joinGame(@PathVariable String gameId) {
        Assert.notNull(playerGameSession.getPlayer());
        Assert.isTrue(quizRunEngine.gameExists(gameId));

        QuizRun quizRun = quizRunEngine.getQuizRun(gameId);
        playerGameSession.setCurrentQuizRun(quizRun);
        return new GameJoinedResponse();
    }


    @RequestMapping(method = RequestMethod.GET, value = "currentQuestion")
    public @ResponseBody QuizPollResponse getCurrentQuestion(HttpServletResponse response) {
        QuizRun quizRun = playerGameSession.getCurrentQuizRun();
        MultipleChoiceQuestion question = quizRun.getCurrentQuestion();
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
