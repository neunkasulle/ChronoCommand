package com.github.neunkasulle.chronocommand.model;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Maria on 28.01.2016.
 * DAO helper class
 */
public class DAOHelper {

    private static final DAOHelper instance = new DAOHelper();

    private SessionFactory sessionFactory;

    private DAOHelper() {

    }

    public static DAOHelper getInstance() {
        return instance;
    }

    public void startup() {
        startup("hibernate.cfg.xml");
    }

    public void startup(String hibernateConfig) {
        sessionFactory = new Configuration().configure(hibernateConfig).buildSessionFactory();
    }

    public void shutdown() {
        sessionFactory.close();
    }

    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
