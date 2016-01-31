package com.github.neunkasulle.chronocommand.model;

import org.hibernate.*;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jannis on 19.01.16.
 */
public class MessageDAO {
    private static final MessageDAO instance = new MessageDAO();

    private MessageDAO() {}

    public static MessageDAO getInstance() {
        return instance;
    }

    /**
     * recieved messages only
     * @param user
     * @return
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

    public boolean saveMessage(Message message) {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(message);
        tx.commit();
        session.flush();
        return true;
    }

}
