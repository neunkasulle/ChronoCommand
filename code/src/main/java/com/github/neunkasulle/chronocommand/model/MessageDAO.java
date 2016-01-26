package com.github.neunkasulle.chronocommand.model;

/**
 * Created by jannis on 19.01.16.
 */
public class MessageDAO {
    private static final MessageDAO instance = new MessageDAO();

    private MessageDAO() {}

    public static MessageDAO getInstance() {
        return instance;
    }

    public Message[] getMessages(User user) {
        throw new UnsupportedOperationException();
    }

    public boolean addMessage(Message message) {
        throw new UnsupportedOperationException();
    }
}
