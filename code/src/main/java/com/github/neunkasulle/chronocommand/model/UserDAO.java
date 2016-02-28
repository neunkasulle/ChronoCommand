package com.github.neunkasulle.chronocommand.model;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jannis on 19.01.16.
 *
 */
public class UserDAO{
    private static final UserDAO instance = new UserDAO();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

    private UserDAO() {

    }

    public static UserDAO getInstance() {
        return instance;
    }

    public User findUser(String username) {
        try {
            org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
            String query = "from User u where u.username = :username";
            Object user = session.createQuery(query).setString("username", username).uniqueResult();
            session.close();
            if (user instanceof User) {
                return (User) user;
            } else if (user == null) {
                LOGGER.warn("No user with username \"{}\" found", username);
            } else {
                LOGGER.error("Failed to get user with username \"{}\" because the object we received is not of type User: {}", username, user);
            }
        } catch (HibernateException e) {
            LOGGER.error("Failed to get user with username \"{}\".", username, e);
        }
        return null;
    }

    public User getUser(Long userId) {
        try {
            org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
            User user = session.get(User.class, userId);
            session.close();
            if (user == null) {
                LOGGER.error("No user with id {} found.", userId);
            }
            return user;
        } catch (HibernateException e) {
            LOGGER.error("Failed to get user with id {}.", userId, e);
            return null;
        }
    }

    public boolean saveUser(User user) {
        try {
            org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
            if (session.contains(user.roles)) {
                session.evict(user.roles);
            }
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
            session.flush();
            session.close();
            return true;
        } catch (HibernateException e) {
            LOGGER.error("Failed to save user \"{}\".", user.getEmail(), e);
            return false;
        }
    }

    public List<User> getUsersBySupervisor(User supervisor) {
        try {
            org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
            List objlist = session.createCriteria(User.class)
                    .add(Restrictions.eq("supervisor", supervisor))
                    .list();
            session.close();
            if (objlist.isEmpty()) {
                LOGGER.warn("No user with supervisor \"{}\" found", supervisor.getEmail());
            }
            List<User> users = new ArrayList<>(objlist.size());
            for (Object obj : objlist) {
                if (obj instanceof User) {
                    users.add((User) obj);
                } else {
                    LOGGER.error("Element not instance of User: {}", obj.toString());
                }
            }
            return users;
        } catch (HibernateException e) {
            LOGGER.error("Failed to get users with supervisor \"{}\".", supervisor.getEmail(), e);
        }
        return new ArrayList<>();
    }

    public List<User> getAllUsers() {
        try {
            org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
            List objlist = session.createCriteria(User.class).list();
            session.close();
            if (objlist.isEmpty()) {
                LOGGER.warn("No users found");
            }
            List<User> users = new ArrayList<>(objlist.size());
            for (Object obj : objlist) {
                if (obj instanceof User) {
                    users.add((User) obj);
                } else {
                    LOGGER.error("Element not instance of User: {}", obj.toString());
                }
            }
            return users;
        } catch (HibernateException e) {
            LOGGER.error("Failed to get all users.", e);
        }
        return new ArrayList<>();
    }

    public User findUserByEmail(String email) {
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("email", email));
        Object obj = criteria.uniqueResult();
        session.close();
        return (User) obj;
    }

    public List<User> getUsersByRole(Role role) {
        List<User> roleUser = new ArrayList<>();
        for (User user : getAllUsers()) {
            if (user.getPrimaryRole().equals(role)) {
                roleUser.add(user);
            }
        }
        return roleUser;
    }

    public Role getRoleByName(String name) {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Object role = session.createCriteria(Role.class).add(Restrictions.eq("name", name)).uniqueResult();
        session.close();
        return (Role) role;
    }

    public List<Role> getAllRoles() {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        List objlist = session.createCriteria(Role.class).list();
        session.close();
        List<Role> roleList = new ArrayList<>(objlist.size());
        for (Object obj : objlist) {
            roleList.add((Role) obj);
        }
        return roleList;
    }

    public void saveRole(Role role) {
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(role);
        tx.commit();
        session.flush();
        session.close();
    }
}
