package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Category;
import com.github.neunkasulle.chronocommand.model.CategoryDAO;
import com.github.neunkasulle.chronocommand.model.DAOHelper;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import com.github.neunkasulle.chronocommand.model.User;
import org.hibernate.cfg.NotYetImplementedException;


/**
 * Created by Janze on 18.01.2016.
 * Controling startup and shutdown
 */
public class MainControl extends Control {


    private static MainControl ourInstance = new MainControl();

    private MainControl() {
        realm = new com.github.neunkasulle.chronocommand.security.Realm();
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
