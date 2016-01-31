package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
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
        throw new NotYetImplementedException();
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

        Role role = new Role("user");
        UserDAO.getInstance().saveRole(role);

        User tom = new User(role, "tom", "tom@chronocommand.eu", "cat", null, 5);
        UserDAO.getInstance().saveUser(tom);

        User matt = new User(role, "matt", "matt@example.com", "matt", tom, 10);
        UserDAO.getInstance().saveUser(matt);
    }

    public void shutdown() {
        DAOHelper.getInstance().shutdown();

        // TODO initiate anything that needs initiating
    }
}
