package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;

import javax.annotation.Nullable;

/**
 * Created by Janze on 18.01.2016.
 * Controls the Creation of new users
 *
 */
public class CreateUserControl extends Control {
    private static CreateUserControl ourInstance = new CreateUserControl();
    
    private CreateUserControl() {

    }

    /**
     * Gets the one Instance of the CreateUserControl.
     * @return The one CreateUserControl instance.
     */
    public static CreateUserControl getInstance() {
        return ourInstance;
    }

    /**
     * Creates a new User and passes it to the DB
     * @param userType The initial Role of the User
     * @param name  Username for the new User. Must be unique.
     * @param email Email adress of the new User. Must be unique
     * @param password Password of the new User.
     * @param supervisor User which  is supervising this new user. Can be null
     * @param hoursPerMonth Numbers the new user has to work to fulfill his contract.
     * @throws ChronoCommandException If there are Errors with the user creation.
     */
    public void createUser(Role userType, String name, String email, String password, @Nullable User supervisor,
                              int hoursPerMonth) throws ChronoCommandException {
        UserDAO userDAO = UserDAO.getInstance();

        if(userDAO.findUser(name) != null) {
            throw new ChronoCommandException(Reason.USERALREADYEXISTS);
        }

        if (userDAO.findUserByMail(email) != null) {
            throw new ChronoCommandException(Reason.EMAILALREADYINUSE);
        }

        userDAO.saveUser(new User(userType, name, email, password, supervisor, hoursPerMonth));
    }

}