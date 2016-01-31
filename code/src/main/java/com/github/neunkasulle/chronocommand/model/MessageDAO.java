package com.github.neunkasulle.chronocommand.model;

import org.hibernate.*;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jannis on 19.01.16.
 * Data access object for messages
 */
public class MessageDAO {
    private static final MessageDAO instance = new MessageDAO();

    private MessageDAO() {}

    /**
     * Gets the one MessageDAO instance
     * @return The one MessageDAO instance
     */
    public static MessageDAO getInstance() {
        return instance;
    }

    /**
     * Gets received messages
     * @param user the receiving user
     * @return a list of messages for the receiving user
     */
    public List<Message> getMessages(User user) {
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Message.class).add(Restrictions.eq("Recipient", user));
        List<Message> messageList = new ArrayList<>();
        for (Object obj : criteria.list()) {
            if (obj instanceof Message) {
                messageList.add((Message) obj);
            }
        }
        return messageList;

    }

    /**
     * Saves a meassage
     * @param message the message which is to be saved
     */
    public void saveMessage(Message message) {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(message);
        tx.commit();
        session.flush();
    }

}
