package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Message;
import com.github.neunkasulle.chronocommand.model.Session;
import com.github.neunkasulle.chronocommand.model.User;
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



    private boolean deliverMessage(Message message, User receiver) {
        //TODO real implementation

        if(SessionControl.getInstance().getSession(receiver) != null) {

        }

        return false;

    }

    private boolean sendMessageAsMail(Message message) {

        throw new NotYetImplementedException();
    }

    public void generateSystemMessages() {
        throw new NotYetImplementedException();
    }

    public void sendMessage(Session session, String receiver, String messageText) {
        UserManagementControl userManagementControl = UserManagementControl.getInstance();

        User sendigUser = userManagementControl.getUser(session);
        User receivingUser = userManagementControl.findUser(receiver);

        Message message = new Message(sendigUser, receivingUser, messageText);

        if(!deliverMessage(message, receivingUser)) {
            //If user Offline store message and try later;
            messageQueue.add(message);
        }

        if(receivingUser.getMailFlag()) {
            sendMessageAsMail(message);
        }
    }
}
