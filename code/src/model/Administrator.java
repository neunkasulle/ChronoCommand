package model;

/**
 * Created by Janze on 16.01.2016.
 */
public class Administrator extends User
{
    public Administrator(String username, String email, String password)
    {
        super(username, email, password);
        role = Role.ADMIN;
    }

}
