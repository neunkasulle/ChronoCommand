package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Message;
import com.github.neunkasulle.chronocommand.model.Session;

import java.util.Queue;

/**
 * Created by Janze on 18.01.2016.
 */
public class MessageControl {
    private Queue<Message> messageQueue;

    private boolean deliverMessage(Message message) {

        return false;
    }

    private boolean sendMessageAsMail(Message message) {

        return false;
    }

    public void generateSystemMessages() {

    }

    public void sendMessage(Session session, String receiver, String messageText) {

    }
}
