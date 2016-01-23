package com.github.neunkasulle.chronocommand.model;



/**
 * Created by jannis on 19.01.16.
 */
public class UserDAO{


    public User findUser(String username) {

        String query = "from User u where u.username = :username";
        //TODO team menber who has to do hibernate: make this work
        //return (User) getSession().createQuery(query).setString("username", username).uniqueResult();
        throw new UnsupportedOperationException();
    }

    public User getUser(Long userId) {
        //return (User) getSession().get(User.class, userId);
        throw new UnsupportedOperationException();
        //TODO FInd unser by ID
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
