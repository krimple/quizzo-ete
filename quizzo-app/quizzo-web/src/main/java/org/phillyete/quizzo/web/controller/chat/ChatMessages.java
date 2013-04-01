package org.phillyete.quizzo.web.controller.chat;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kenrimple
 * Date: 3/31/13
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChatMessages {

    private int nextIndex;
    private List<String> messages;

    public ChatMessages(int nextIndex, List<String> messages) {
        this.nextIndex = nextIndex;
        this.messages = messages;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public List<String> getMessages() {
        return messages;
    }

}
