package org.springframework.samples.async.quizzo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.async.quizzo.controller.moderator.*;
import org.springframework.samples.async.quizzo.controller.moderator.command.GameCommand;
import org.springframework.samples.async.quizzo.controller.moderator.command.ModeratorCommand;
import org.springframework.samples.async.quizzo.engine.GameRunEngine;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/game/")
public class QuizModeratorController extends AbstractQuizController {

  private GameRunEngine gameRunEngine;

  @Autowired
  public QuizModeratorController(GameRunEngine gameRunEngine) {
    this.gameRunEngine = gameRunEngine;

  }

  @RequestMapping(method = RequestMethod.POST, value="startGame",
                  consumes = "application/json",
                  produces = "application/json")
  public @ResponseBody QuizModeratorResponse startQuiz(@RequestBody ModeratorCommand command) {

      Assert.notNull(command.getQuizId());
      return startQuiz(command.getGameTitle(), command.getQuizId());
  }

  @RequestMapping(method = RequestMethod.POST, value = "moderate",
          consumes = "application/json",
          produces = "application/json")
  public @ResponseBody QuizModeratorResponse moderate(@RequestBody GameCommand command,
                                 HttpSession session,
                                 HttpServletResponse response) {
    // extremely lame-o command-lite pattern
    String commandValue = command.getCommand();
    Assert.notNull(commandValue);
    Assert.notNull(command.getGameId());

    QuizModeratorResponse commandResponse = new OkModeratorResponse();
    if (commandValue.equals("BeginPlay")) {
        beginPlay(command.getGameId());
    } else if (commandValue.equals("NextQuestion")) {
        moveToNextQuestion(command.getGameId());
    } else if (commandValue.equals("EndQuestion")) {
        endQuestion(command.getGameId());
    } else if (commandValue.equals("EndGame")) {
        endGame(command.getGameId());
    } else if (commandValue.equals("DestroyGame")) {
        destroyGame(command.getGameId());
    } else {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return null;
    }
    return commandResponse;
  }

  private QuizModeratorResponse startQuiz(String title, String quizId) {
    String runName = title == null ?
        "new game for " + quizId + " at " + new Date().toString() : title;

    // TODO - verify there is no existing quiz run with this quizRunName...  mongo unique index?
    String gameId = gameRunEngine.startQuizRunAndBeginTakingPlayers(quizId, runName);

    if (gameId != null) {
      return new GameStartedResponse(gameId);
    } else {
      return new GameNotStartedResponse();
    }
  }

  private void beginPlay(String gameId) {
    Assert.notNull(gameId);
    gameRunEngine.stopTakingPlayersAndStartGamePlay(gameId);
  }

  private void moveToNextQuestion(String gameId) {
    Assert.notNull(gameId);
    gameRunEngine.moveToNextQuestion(gameId);
  }

  private void endQuestion(String gameId) {
    gameRunEngine.endQuestion(gameId);
  }


  private void endGame(String gameId) {
    gameRunEngine.endQuiz(gameId);
  }

  private void destroyGame(String gameId) {
    gameRunEngine.destroyQuizRun(gameId);
  }
}
