package org.phillyete.quizzo.web.controller.gamestatus;

import org.phillyete.quizzo.web.controller.gamestatus.GameStatus;

public class NoGameSessionCreated extends GameStatus {
  public NoGameSessionCreated() {
    this.setCurrentScore(0);
    this.setStatus("NoGameSessionCreated");
  }
}
