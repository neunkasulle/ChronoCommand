package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Janze on 06.02.2016.
 */
public class CreateUserControlTest {

    @Before
    public void setUp() throws Exception {
        MainControl mainControl = MainControl.getInstance();


        mainControl.startup();
    }

    @Test
    public void testCreateUser() throws Exception {
        CreateUserControl createUserControl;
        UserDAO userDAO = UserDAO.getInstance();

        createUserControl = CreateUserControl.getInstance();

        createUserControl.createUser(new Role("admin"),"Chutulu", "chutulu@eatsyour.soul","1234", " ", null, 999999999);

        assert(userDAO.findUser("Chutulu").getRealname().equals(" "));

    }
}