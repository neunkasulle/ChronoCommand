package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;

/**
 * Created by Janze on 18.01.2016.
 * User creation Stuff
 */
public class CreateUserControl extends Control {
    private static CreateUserControl ourInstance = new CreateUserControl();
    
    private CreateUserControl() {

    }



    public static CreateUserControl getInstance() {
        return ourInstance;
    }


    public boolean createUser(Role userType, String name, String email, String password, User supervisor,
                              int hoursPerMonth) throws ChronoCommandException {
        UserDAO userDAO = UserDAO.getInstance();

        if(userDAO.findUser(name) != null) {
            throw new ChronoCommandException(Reason.USERALREADYEXISTS);
        }

        if (userDAO.findUserByMail(email) != null) {
            throw new ChronoCommandException(Reason.EMAILALREADYINUSE);
        }

        userDAO.saveUser(new User(userType, name, email, password, supervisor, hoursPerMonth));

        return true;
    }

}