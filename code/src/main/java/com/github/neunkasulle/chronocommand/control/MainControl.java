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

    /**
     * Gets the one Instance of the MainControl.
     * @return The one MainControl instance.
     */
    public static MainControl getInstance() {
        return ourInstance;
    }

    /**
     * Handles Exceptions which are not handled on invocation.
     * When application crashed critical, it will save and shut down.
     */
    private void exceptionHandling() {
        throw new NotYetImplementedException();
    }

    /**
     * Initializes the application with all the needed
     */
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


    /**
     * Saves necessary data before shutting down
     */
    public void shutdown() {
        DAOHelper.getInstance().shutdown();
        System.exit(0);
        // TODO initiate anything that needs initiating
    }
}
