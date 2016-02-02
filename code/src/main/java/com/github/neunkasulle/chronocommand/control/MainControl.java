package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.hibernate.cfg.NotYetImplementedException;

import java.time.LocalDateTime;
import java.time.Month;


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
        throw new NotYetImplementedException(); //TODO
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
        try {
            CategoryDAO.getInstance().saveCategory(new Category("Programming"));
            CategoryDAO.getInstance().saveCategory(new Category("Procrastination"));
        } catch(ChronoCommandException e) {}

        Role role = new Role("user");
        UserDAO.getInstance().saveRole(role);

        User tom = new User(role, "tom", "Tom", "tom@chronocommand.eu", "cat", null, 5);
        UserDAO.getInstance().saveUser(tom);

        User matt = new User(role, "matt", "Matt", "matt@example.com", "matt", tom, 10);
        UserDAO.getInstance().saveUser(matt);

        TimeSheet tomTimeSheet = new TimeSheet(UserDAO.getInstance().findUser("tom"), Month.JANUARY, 2016);

        Category prog = CategoryDAO.getInstance().findCategoryByString("Programming");
        String taet = "codework for PSE";
        LocalDateTime date1 = LocalDateTime.of(2016, 1, 1, 11, 30);
        LocalDateTime date2 = LocalDateTime.of(2016, 1, 1, 15, 30);
        TimeRecord timeRecTom = new TimeRecord( date1, date2, prog, taet);

        tomTimeSheet.addTime(timeRecTom);
        TimeSheetDAO.getInstance().saveTimeSheet(tomTimeSheet);
    }


    /**
     * Saves necessary data before shutting down
     */
    public void shutdown() {
        DAOHelper.getInstance().shutdown();

        // TODO initiate anything that needs initiating
    }
}
