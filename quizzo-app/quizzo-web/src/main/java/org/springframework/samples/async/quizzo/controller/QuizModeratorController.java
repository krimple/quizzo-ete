package org.springframework.samples.async.quizzo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.async.quizzo.controller.moderator.*;
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

  @RequestMapping(method = RequestMethod.POST, value="startGame/{quizId}")
  public QuizModeratorResponse startQuiz(@RequestBody ModeratorCommand command) {

      Assert.notNull(command.getQuizId());
      return startQuiz(command.getGameTitle(), command.getQuizId());
  }

  @RequestMapping(method = RequestMethod.POST, value = "moderate")
  public
  @ResponseBody
  QuizModeratorResponse moderate(@RequestBody ModeratorCommand command,
                                 HttpSession session,
                                 HttpServletResponse response) {
    // extremely lame-o command-lite pattern
    ModeratorCommands commandValue = command.getCommand();
    Assert.notNull(command.getGameId());

    QuizModeratorResponse commandResponse = new OkModeratorResponse();
    switch (commandValue) {
      case BEGIN_PLAY:
        beginPlay(command.getGameId());
        break;
      case NEXT_QUESTION:
        // todo - when no more questions?
        moveToNextQuestion(command.getGameId());
        break;
      case END_QUESTION:
        endQuestion(command.getGameId());
        break;
      case END_GAME:
        endGame(command.getGameId());
        break;
      case DESTROY_GAME:
        destroyGame(command.getGameId());
        break;
      default:
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
