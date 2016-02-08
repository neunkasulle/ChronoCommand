package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Reason;
import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Janze on 06.02.2016.
 */
public class CreateUserControlTest extends UeberTest {

    @Test
    public void testCreateUser() throws Exception {
        CreateUserControl createUserControl;
        UserDAO userDAO = UserDAO.getInstance();

        createUserControl = CreateUserControl.getInstance();

        Role admin = new Role("admin");
        UserDAO.getInstance().saveRole(admin);

        createUserControl.createUser(admin,"Chutulu", "chutulu@eatsyour.soul","1234", " ", null, 999999999);

        assert(userDAO.findUser("Chutulu").getRealname().equals(" "));

    }

    @Test
    public void testCreateExistingUser() {
        CreateUserControl createUserControl;
        createUserControl = CreateUserControl.getInstance();
        try {
            createUserControl.createUser(new Role("admin"),"tom", "chutulu@eatsyour.soul","1234", " ", null, 999999999);
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.USERALREADYEXISTS);
        }
    }

    @Test
    public void testCreateExistingEmail() {
        CreateUserControl createUserControl;
        createUserControl = CreateUserControl.getInstance();
        try {
            createUserControl.createUser(new Role("admin"),"...", "tom@chronocommand.eu","1234", " ", null, 999999999);
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.EMAILALREADYINUSE);
        }
    }
}