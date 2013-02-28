package org.springframework.samples.async.quizzo.controller.moderator;

// todo - subclass?
public class ModeratorCommand {
  private ModeratorCommands command;
  private String quizId;
  private String gameTitle;
  private String nickName;
  private String gameId;

  public ModeratorCommands getCommand() {
    return command;
  }

  public void setCommand(ModeratorCommands command) {
    this.command = command;
  }

  public String getQuizId() {
    return quizId;
  }

  public void setQuizId(String quizId) {
    this.quizId = quizId;
  }

  public String getGameTitle() {
    return gameTitle;
  }

  public void setGameTitle(String gameTitle) {
    this.gameTitle = gameTitle;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }
}
