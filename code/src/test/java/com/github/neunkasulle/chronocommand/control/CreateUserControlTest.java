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

        Role admin = new Role("admin", "a", true);
        UserDAO.getInstance().saveRole(admin);

        createUserControl.createUser(admin,"Chutulu", "chutulu@eatsyour.soul","1234", "dè real Ĉthulh⊂", null, 80);

        assertTrue(userDAO.findUser("Chutulu").getRealname().equals("dè real Ĉthulh⊂"));

    }

    @Test(expected = ChronoCommandException.class)
    public void testCreateExistingUser() throws ChronoCommandException{
        CreateUserControl createUserControl;
        createUserControl = CreateUserControl.getInstance();
        createUserControl.createUser(new Role("admin", "a", true),"tom", "chutulu@eatsyour.soul","1234", "dè real Ĉthulh⊂", null, 999999999);
    }

    @Test(expected = ChronoCommandException.class)
    public void testCreateExistingEmail()throws ChronoCommandException {
        CreateUserControl createUserControl;
        createUserControl = CreateUserControl.getInstance();
        createUserControl.createUser(new Role("admin", "a", true),"...", "tom@chronocommand.eu","1234", "dè real Ĉthulh⊂", null, 999999999);
    }
}