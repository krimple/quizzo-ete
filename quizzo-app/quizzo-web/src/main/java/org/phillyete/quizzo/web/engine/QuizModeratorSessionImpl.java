package org.phillyete.quizzo.web.engine;

public class QuizModeratorSessionImpl implements QuizModeratorSession {

    String nickName;

    String gameId;

    @Override
    public String getNickName() {
        return nickName;
    }

    @Override
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String getGameId() {
        return gameId;
    }

    @Override
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
