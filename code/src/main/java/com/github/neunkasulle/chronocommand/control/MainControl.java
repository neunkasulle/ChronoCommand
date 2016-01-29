package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.UserDAO;
import com.github.neunkasulle.chronocommand.model.Category;
import com.github.neunkasulle.chronocommand.model.CategoryDAO;
import com.github.neunkasulle.chronocommand.model.DAOHelper;
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

        CategoryDAO.getInstance().newCategory(new Category("ChronoCommand"));
        CategoryDAO.getInstance().newCategory(new Category("Procrastination"));

        // TODO initiate anything that needs initiating
    }

    public void shutdown() {
        DAOHelper.getInstance().shutdown();

        // TODO initiate anything that needs initiating
    }
}
