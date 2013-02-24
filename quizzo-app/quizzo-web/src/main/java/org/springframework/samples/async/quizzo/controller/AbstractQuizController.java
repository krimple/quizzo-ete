package org.springframework.samples.async.quizzo.controller;

import org.springframework.samples.async.quizzo.engine.PlayerGameSession;
import org.springframework.samples.async.quizzo.engine.PlayerGameSessionImpl;
import org.springframework.samples.async.quizzo.engine.QuizModeratorSession;
import org.springframework.samples.async.quizzo.engine.QuizModeratorSessionImpl;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/22/13
 * Time: 11:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractQuizController {

    protected void sendHttpStatusResponse(int statusCode, String message, HttpServletResponse response) {
        try {
            response.sendError(statusCode, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected PlayerGameSession getOrCreatePlayerGameSession(HttpSession session) {
        PlayerGameSession playerGameSession = (PlayerGameSession) session.getAttribute("playerGameSession");
        if (playerGameSession == null) {
            playerGameSession = new PlayerGameSessionImpl();
            session.setAttribute("playerGameSession", playerGameSession);
        }
        return playerGameSession;
    }

    protected QuizModeratorSession getOrCreateQuizModeratorSession(HttpSession session) {
        QuizModeratorSession quizModeratorSession = (QuizModeratorSession) session.getAttribute("quizModeratorSession");
        if (quizModeratorSession == null) {
            quizModeratorSession = new QuizModeratorSessionImpl();
            session.setAttribute("quizModeratorSession", quizModeratorSession);
        }
        return quizModeratorSession;
    }
}
