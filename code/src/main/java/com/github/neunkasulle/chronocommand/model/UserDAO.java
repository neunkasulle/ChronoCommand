package com.github.neunkasulle.chronocommand.model;

/**
 * Created by jannis on 19.01.16.
 */
public class UserDAO {
    public User findUser(String username) {
        throw new UnsupportedOperationException();
    }

    public boolean addUser(User newuser) {
        throw new UnsupportedOperationException();
    }

    public boolean deleteUser(User user) {
        throw new UnsupportedOperationException();
    }

    public Proletarier[] getProletarierBySupervisor(Supervisor supervisor) {
        throw new UnsupportedOperationException();
    }

    public Proletarier[] getAllProletarier() {
        throw new UnsupportedOperationException();
    }

    public boolean checkUserDetails(Role userRole, String name, String email,
                                    String password, Supervisor supervisor,
                                    int hoursPerMonth) {
        throw new UnsupportedOperationException();
    }
}