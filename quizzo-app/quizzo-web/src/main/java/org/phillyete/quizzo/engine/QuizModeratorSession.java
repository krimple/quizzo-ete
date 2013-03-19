package org.phillyete.quizzo.engine;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/24/13
 * Time: 2:16 AM
 * To change this template use File | Settings | File Templates.
 */
public interface QuizModeratorSession {
    String getNickName();

    void setNickName(String nickName);

    String getGameId();

    void setGameId(String gameId);
}
