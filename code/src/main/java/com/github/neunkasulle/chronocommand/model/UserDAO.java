package com.github.neunkasulle.chronocommand.model;

import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jannis on 19.01.16.
 */
public class UserDAO{
    private static final UserDAO instance = new UserDAO();
    private Logger log = LoggerFactory.getLogger(UserDAO.class);

    private UserDAO() {}

    public static UserDAO getInstance() {
        return instance;
    }

    public User findUser(String username) {
        try {
            org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
            String query = "from User u where u.username = :username or u.email = :username";
            Object user = session.createQuery(query).setString("username", username).uniqueResult();
            if (user instanceof User) {
                return (User) user;
            } else if (user == null) {
                log.warn("No user with username \"{}\" found", username);
            } else {
                log.error("Failed to get user with username \"{}\" because the object we received is not of type User: {}", username, user);
            }
        } catch (HibernateException e) {
            log.error("Failed to get user with username \"{}\".", username, e);
        }
        return null;
    }

    public User getUser(Long userId) {
        try {
            org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
            User user = session.get(User.class, userId);
            if (user == null) {
                log.error("No user with id {} found.", userId);
            }
            return user;
        } catch (HibernateException e) {
            log.error("Failed to get user with id {}.", userId, e);
            return null;
        }
    }

    public boolean saveUser(User user) {
        try {
            org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
            session.flush();
            return true;
        } catch (HibernateException e) {
            log.error("Failed to save user \"{}\".", user.getEmail(), e);
            return false;
        }
    }

    public List<User> getProletarierBySupervisor(User supervisor) {
        try {
            org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
            List objlist = session.createCriteria(User.class)
                    .add(Restrictions.eq("supervisor_id", supervisor.getId()))
                    .list();
            if (objlist.isEmpty()) {
                log.warn("No user with supervisor \"{}\" found", supervisor.getEmail());
            }
            List<User> users = new ArrayList<>(objlist.size());
            for (Object obj : objlist) {
                if (obj instanceof User) {
                    users.add((User) obj);
                } else {
                    log.error("Element not instance of User: {}", obj.toString());
                }
            }
            return users;
        } catch (HibernateException e) {
            log.error("Failed to get users with supervisor \"{}\".", supervisor.getEmail(), e);
        }
        return new ArrayList<>();
    }

    public boolean checkUserDetails(Role userRole, String name, String email, User supervisor, int hoursPerMonth) {
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        if (session.get(Role.class, userRole.getId()) == null ) {
            return false;
        }
        Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("name", name));
        if (criteria.list().size() > 0) {
            return false;
        }
        if ( !email.contains("@") || !email.contains(".") ) {
            return false;
        }
        criteria = session.createCriteria(User.class).add(Restrictions.eq("email", email));
        if (criteria.list().size() > 0) {
            return false;
        }
        if (session.get(User.class, supervisor.getId()) == null) {
            return false;
        }
        if ( hoursPerMonth < 0 || hoursPerMonth > 80) {
            return false;
        }
        return true;
    }

    public List<User> getAllUsers() {
        try {
            org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
            List objlist = session.createCriteria(User.class).list();
            if (objlist.isEmpty()) {
                log.warn("No users found");
            }
            List<User> users = new ArrayList<>(objlist.size());
            for (Object obj : objlist) {
                if (obj instanceof User) {
                    users.add((User) obj);
                } else {
                    log.error("Element not instance of User: {}", obj.toString());
                }
            }
            return users;
        } catch (HibernateException e) {
            log.error("Failed to get all users.", e);
        }
        return new ArrayList<>();
    }

    public User findUserByMail(String email) {
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("email", email));
        Object obj = criteria.uniqueResult();
        return (User) obj;
    }

}
