package org.springframework.samples.async.quizzo.controller.moderator.command;

public abstract class ModeratorCommand {
  private String command;
  private String quizId;
  private String gameTitle;
  private String nickName;

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
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


}
