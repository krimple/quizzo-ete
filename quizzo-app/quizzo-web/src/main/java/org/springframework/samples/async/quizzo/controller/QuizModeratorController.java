package org.springframework.samples.async.quizzo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.async.quizzo.engine.QuizModeratorSession;
import org.springframework.samples.async.quizzo.engine.GameRunEngine;
import org.springframework.samples.async.quizzo.responses.GameNotStartedResponse;
import org.springframework.samples.async.quizzo.responses.QuizPollResponse;
import org.springframework.samples.async.quizzo.responses.QuizStartedResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/moderator/game")
public class QuizModeratorController extends AbstractQuizController {

    private GameRunEngine gameRunEngine;

    @Autowired
    public QuizModeratorController(GameRunEngine gameRunEngine) {
        this.gameRunEngine = gameRunEngine;

    }

    @RequestMapping(method = RequestMethod.POST, value = "startGame/{quizId}/moderator/{moderatorNickName}")
    public @ResponseBody QuizPollResponse startGame(@PathVariable String quizId,
                                                    @PathVariable String moderatorNickName,
                                                    @RequestParam(required = false) String gameName,
                                                    HttpSession session,
                                                    HttpServletResponse response) {

        QuizModeratorSession quizModeratorSession = getOrCreateQuizModeratorSession(session);
        // TODO this feels like it needs to go into a quiz moderator service... Too fine grained here.
        Assert.notNull(moderatorNickName, "Must contain a moderator nick");
        String runName = gameName == null ?
                "new game for " + quizId + " at " + new Date().toString() : gameName;

        // TODO - srp violated - move this to another method and guard
        quizModeratorSession.setNickName(moderatorNickName);

        // TODO - verify there is no existing quiz run with this quizRunName...
        String gameId = gameRunEngine.startQuizRunAndBeginTakingPlayers(quizId, runName);

        if (gameId != null) {
            // never leak this - it is kept at the user session level
            // so that it cannot be spoofed
            quizModeratorSession.setGameId(gameId);
            sendHttpStatusResponse(HttpStatus.OK.value(), "created.", response);
            return new QuizStartedResponse();
        } else {
            return new GameNotStartedResponse();
        }
    }

    // move to next question
    @RequestMapping(method = RequestMethod.POST, value = "nextQuestion")
    public void moveToNextQuestion(HttpSession session) {
        QuizModeratorSession quizModeratorSession = getOrCreateQuizModeratorSession(session);
        Assert.notNull(quizModeratorSession.getNickName());
        Assert.notNull(quizModeratorSession.getGameId());
        gameRunEngine.moveToNextQuestion(quizModeratorSession.getGameId());
    }

    // begin play
    @RequestMapping(method = RequestMethod.POST, value="beginPlay")
    @ResponseStatus(HttpStatus.OK)
    public void beginPlay(HttpSession session) {
        QuizModeratorSession quizModeratorSession = getOrCreateQuizModeratorSession(session);
        Assert.notNull(quizModeratorSession.getNickName());
        Assert.notNull(quizModeratorSession.getGameId());
        gameRunEngine.stopTakingPlayersAndStartGamePlay(quizModeratorSession.getGameId());
    }

    // end current question
    @RequestMapping(method = RequestMethod.POST, value="endAnswers")
    public void endAnswers(HttpSession session) {
        QuizModeratorSession quizModeratorSession = getOrCreateQuizModeratorSession(session);

        Assert.notNull(quizModeratorSession.getNickName());
        Assert.notNull(quizModeratorSession.getGameId());
        gameRunEngine.endQuestion(quizModeratorSession.getGameId());
    }

    // todo
    // get statistics


    // end game - you can still review your score
    @RequestMapping(method = RequestMethod.POST, value="endGame")
    public void endGame(HttpSession session) {
        QuizModeratorSession quizModeratorSession = getOrCreateQuizModeratorSession(session);
        Assert.notNull(quizModeratorSession.getNickName());
        Assert.notNull(quizModeratorSession.getGameId());
        gameRunEngine.endQuiz(quizModeratorSession.getGameId());
    }

    // todo
    // remove game from quiz run engine - it's gone and nobody else can run it
    @RequestMapping(method = RequestMethod.POST, value="endQuizRun")
    public void destroyQuizRun(HttpSession session) {
        QuizModeratorSession quizModeratorSession = getOrCreateQuizModeratorSession(session);
        Assert.notNull(quizModeratorSession.getNickName());
        Assert.notNull(quizModeratorSession.getGameId());
        gameRunEngine.destroyQuizRun(quizModeratorSession.getGameId());
        quizModeratorSession.setGameId(null); // fly, be free!
    }
}
