package model;

/**
 * Created by Janze on 16.01.2016.
 */
public class Supervisor extends Proletarier
{
    public Supervisor(String username, String email, String password, String fullName, int hoursPerMonth)
    {
        super(username, email, password, fullName, null, hoursPerMonth);
        role = Role.SUPERVISOR;
    }

}
