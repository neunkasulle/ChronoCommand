package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Category;
import com.github.neunkasulle.chronocommand.model.CategoryDAO;
import com.github.neunkasulle.chronocommand.model.DAOHelper;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import com.github.neunkasulle.chronocommand.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.hibernate.cfg.NotYetImplementedException;


/**
 * Created by Janze on 18.01.2016.
 * Controling startup and shutdown
 */
public class MainControl extends Control {


    private static MainControl ourInstance = new MainControl();

    private MainControl() {

    }

    public static MainControl getInstance() {
        return ourInstance;
    }



    private void exceptionHandling() {
        throw new NotYetImplementedException(); //TODO
    }

    public void startup() {
        DAOHelper.getInstance().startup();

        // TODO initiate anything that needs initiating

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // DEBUG fill database with data
        CategoryDAO.getInstance().saveCategory(new Category("Programming"));
        CategoryDAO.getInstance().saveCategory(new Category("Procrastination"));

        User tom = new User();
        tom.setEmail("tom@chronocommand.eu");
        tom.setPassword("cat");
        tom.setUsername("tom");
        UserDAO.getInstance().saveUser(tom);

        User matt = new User();
        matt.setEmail("matt@example.com");
        matt.setPassword("matt");
        matt.setUsername("matt");
        matt.setSupervisor(tom);
        UserDAO.getInstance().saveUser(matt);
    }

    public void shutdown() {
        DAOHelper.getInstance().shutdown();

        // TODO initiate anything that needs initiating
    }
}
