package org.phillyete.quizzo.web.engine;

public interface QuizModeratorSession {
    String getNickName();

    void setNickName(String nickName);

    String getGameId();

    void setGameId(String gameId);
}
