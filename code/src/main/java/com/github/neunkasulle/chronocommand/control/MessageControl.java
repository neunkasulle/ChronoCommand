package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
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

    /**
     * Gets the one Instance of the MessageControl.
     * @return The one MessageControl instance.
     */
    public static MessageControl getInstance() {
        return ourInstance;
    }


    /**
     * Delivers a message to a given user
     * @param message the message which is to be delivered
     * @param receiver the recipient of the message
     * @return false if the user is not online to receive the message
     */
    private boolean deliverMessage(Message message, User receiver) {
        //TODO real implementation

        if(SessionControl.getInstance().getSession(receiver) == null) {
            return false;
        }

        return true;
    }

    /**
     * Messages send via mail to a user
     * @param message the Message which is to be send
     */
    private void sendMessageAsMail(Message message) {

        throw new NotYetImplementedException();
    }

    /**
     * Messages from the System itself will be generated
     */
    public void generateSystemMessages(User user) {
        String message = "The System says.";
        Message sysMessage = new Message(null, user ,message);

        throw new NotYetImplementedException();
    }

    /**
     * Send a message from a user to a user
     * @param session the session of the sender
     * @param receiver the recipient
     * @param messageText the text in the message
     */
    public void sendMessage(Session session, String receiver, String messageText) {
        UserManagementControl userManagementControl = UserManagementControl.getInstance();

        User sendigUser = userManagementControl.getUser(session);
        User receivingUser = userManagementControl.findUser(receiver);

        Message message = new Message(sendigUser, receivingUser, messageText);

        if(!deliverMessage(message, receivingUser)) {
            //If user Offline store message and try later;
            messageQueue.add(message); //TODO messageQueue Job implementiern
        }

        if(receivingUser.getMailFlag()) {
            sendMessageAsMail(message);
        }
    }
}
