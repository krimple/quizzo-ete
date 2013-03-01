package org.springframework.samples.async.quizzo.controller.moderator.command;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 2/28/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class NextQuestion extends GameCommand {
    public NextQuestion() {
        this.setCommand("NextQuestion");
    }
}
