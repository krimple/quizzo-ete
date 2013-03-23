package org.phillyete.quizzo.web.controller.moderator;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/28/13
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ModeratorCommands {
  START_GAME("StartGame"),
  BEGIN_PLAY("BeginPlay"),
  NEXT_QUESTION("NextQuestion"),
  END_QUESTION("EndQuestion"),
  END_GAME("EndGame"),
  DESTROY_GAME("DestroyGame");

  private final String name;
  private ModeratorCommands(String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }
}
