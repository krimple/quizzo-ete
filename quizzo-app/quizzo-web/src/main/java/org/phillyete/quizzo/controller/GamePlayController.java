package org.phillyete.quizzo.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.phillyete.quizzo.domain.Choice;
import org.phillyete.quizzo.domain.MultipleChoiceQuestion;
import org.phillyete.quizzo.domain.PlayerAnswer;
import org.phillyete.quizzo.engine.GameRunEngine;
import org.phillyete.quizzo.engine.PlayerGameSession;
import org.phillyete.quizzo.repository.PlayerAnswerRepository;
import org.phillyete.quizzo.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/quizRun")
public class GamePlayController extends AbstractQuizController {

    private GameRunEngine quizRunEngine;

    private PlayerAnswerRepository playerAnswerRepository;


    @Autowired
    public GamePlayController(GameRunEngine quizRunEngine,
                              PlayerAnswerRepository playerAnswerRepository) {
        this.quizRunEngine = quizRunEngine;
        this.playerAnswerRepository = playerAnswerRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "games")
    public @ResponseBody
    ResponseEntity<List> getGamesAwaitingPlayers () {
        List<HashMap> results = quizRunEngine.getGamesAwaitingPlayers();
        if (results.size() == 0) {
            return new ResponseEntity<List>(HttpStatus.NO_CONTENT);     // 204
        } else {
            return new ResponseEntity<List>(results, HttpStatus.OK);    // 200
        }
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
    public @ResponseBody QuizPollResponse submitPlayerAnswer(
            @RequestBody PlayerAnswer playerAnswer,
            HttpSession session) {

        PlayerGameSession gameSession = getOrCreatePlayerGameSession(session);
        playerAnswer.setGameId(gameSession.getGameId());
        playerAnswer.setPlayerId(gameSession.getPlayerId());

        // make sure we're answering the right question
        MultipleChoiceQuestion question =
                quizRunEngine.getCurrentQuizQuestion(gameSession.getGameId());

        if (playerAnswer.getQuestionNumber() != question.getQuestionNumber()) {
            return new IncorrectQuestionAnsweredResponse();
        }

        // score it
        Choice choice = question.getChoice(playerAnswer.getChoice());
        playerAnswer.setScore(choice.getScore());

        // record quiz answer
        playerAnswerRepository.save(playerAnswer);
        return new AnswerSubmittedResponse();
    }

    @RequestMapping(method = RequestMethod.GET, value="whoami")
    public @ResponseBody WhoAmIResponse tellMeWhoIamAndWhatImDoingHereDude(HttpSession session) {
        PlayerGameSession playerGameSession = this.getOrCreatePlayerGameSession(session);
        return new WhoAmIResponse(playerGameSession);
    }
}
