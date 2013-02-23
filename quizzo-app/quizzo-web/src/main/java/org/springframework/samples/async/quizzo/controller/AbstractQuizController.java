package org.springframework.samples.async.quizzo.controller;

import javax.servlet.http.HttpServletResponse;
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
}
