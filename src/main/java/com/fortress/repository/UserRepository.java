package com.fortress.repository;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.fortress.model.User;

@Repository
public class UserRepository {

    private HashMap<String, User> users = new HashMap<>();

    public void save(String userID, User user) {
        users.put(userID, user);
    }

    public void delete(String userID) {
        users.remove(userID);
    }

    public User findById(String userID) {
        return users.get(userID);
    }
}
