package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDAO {

    List<User> findAll();

    User findByUsername(String username);

    Long findIdByUsername(String username);
    
    User getUserById (Long id);

    boolean create(String username, String password);
}
