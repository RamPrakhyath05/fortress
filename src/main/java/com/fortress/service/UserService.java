package com.fortress.service;

import org.springframework.stereotype.Service;

import com.fortress.model.Role;
import com.fortress.model.User;
import com.fortress.repository.UserRepository;
import com.fortress.util.PasswordHasher;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher hasher;

    private int idCounter = 1;

    public UserService(UserRepository userRepository, PasswordHasher hasher) {
        this.userRepository = userRepository;
        this.hasher = hasher;
    }

    public User getUserDetails(String userID){
        return userRepository.findById(userID);
    }

    public void addUser(String userName, String userPassword, Role role) {

        String userID = String.valueOf(idCounter++);
        String hashedPassword = hasher.getHashedPassword(userPassword);

        User user = new User(userID, userName, hashedPassword, role);

        userRepository.save(userID, user);
    }

    public void deleteUser(String userID) {
        userRepository.delete(userID);
    }

    public boolean verifyPassword(String userID, String userPassword) {

        User user = userRepository.findById(userID);

        if (user == null) {
            return false;
        }

        String hashedInput = hasher.getHashedPassword(userPassword);

        return hashedInput.equals(user.getPassword());
    }
}
