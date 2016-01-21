package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Message;
import com.github.neunkasulle.chronocommand.model.Session;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.Queue;

/**
 * Created by Janze on 18.01.2016.
 * Handles Message creation and delivery
 */
public class MessageControl {
    private Queue<Message> messageQueue;

    private static MessageControl ourInstance = new MessageControl();

    private MessageControl() {

    }

    public static MessageControl getInstance() {
        return ourInstance;
    }



    private boolean deliverMessage(Message message) {

        return false;
    }

    private boolean sendMessageAsMail(Message message) {

        return false;
    }

    public void generateSystemMessages() {
        throw new NotYetImplementedException();
    }

    public void sendMessage(Session session, String receiver, String messageText) {
        throw new NotYetImplementedException();
    }
}
