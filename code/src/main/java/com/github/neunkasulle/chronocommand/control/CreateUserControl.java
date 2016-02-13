package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * Created by Janze on 18.01.2016.
 * Controls the Creation of new users
 *
 */
public class CreateUserControl {
    private static CreateUserControl ourInstance = new CreateUserControl();
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserControl.class);
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
     * @param username  Username for the new User. Must be unique.
     * @param email Email adress of the new User. Must be unique
     * @param password Password of the new User.
     * @param supervisor User which  is supervising this new user. Can be null
     * @param hoursPerMonth Numbers the new user has to work to fulfill his contract.
     * @throws ChronoCommandException If there are Errors with the user creation.
     */
    public void createUser(Role userType, String username, String email, String password, String fullname,
                                @Nullable User supervisor, int hoursPerMonth) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        UserDAO userDAO = UserDAO.getInstance();

        username = username.trim();
        email = email.trim();
        fullname = fullname.trim();

        if(userDAO.findUser(username) != null) {
            LOGGER.error("user" + username + "already exists");
            throw new ChronoCommandException(Reason.USERALREADYEXISTS);
        }

        if (userDAO.findUserByEmail(email) != null) {
            LOGGER.error("email" + email + "is already in use");
            throw new ChronoCommandException(Reason.EMAILALREADYINUSE);
        }

        userDAO.saveUser(new User(userType, username, email, password, fullname, supervisor, hoursPerMonth));
        LOGGER.info("successfully created new user" + username);
    }

}