package com.fortress.service;

import org.springframework.stereotype.Service;

import com.fortress.model.Role;
import com.fortress.model.User;
import com.fortress.repository.UserRepository;
import com.fortress.util.PasswordHasher;

import java.util.List;
import java.util.ArrayList;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher hasher;

    private int idCounter = 1;

    public UserService(UserRepository userRepository, PasswordHasher hasher) {
        this.userRepository = userRepository;
        this.hasher = hasher;
    }

    public User getUserDetails(String userID) {
        return userRepository.findById(userID);
    }

    public User verifyPassword(String userName, String userPassword) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        if (!user.isActive()) {
            throw new RuntimeException("User is inactive");
        }
        String hashedInput = hasher.getHashedPassword(userPassword);
        if (!hashedInput.equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.getAllUsers().values());
    }

    public void addUser(String userName, String userPassword, Role role) {
        User existing = userRepository.findByUserName(userName);
        if (existing != null) {
            throw new RuntimeException("Username already exists");
        }
        String userID = String.valueOf(idCounter++);
        String hashedPassword = hasher.getHashedPassword(userPassword);
        User user = new User(userID, userName, hashedPassword, role, true);
        userRepository.save(userID, user);
    }

    public void deleteUser(String userID, String requesterID) {
        User modifier = userRepository.findById(requesterID);
        if (modifier == null) {
            throw new RuntimeException("Requester not found");
        }
        if (modifier.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access denied, ADMIN only");
        }
        if (!modifier.isActive()) {
            throw new RuntimeException("Inactive user cannot perform actions");
        }
        userRepository.delete(userID);
    }

    public void updateUser(String userID, String newUserName, Role newRole, String requesterID) {
        User modifier = userRepository.findById(requesterID);
        if (modifier == null) {
            throw new RuntimeException("Modifier not found");
        }
        if (modifier.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access denied, ADMIN only");
        }
        if (!modifier.isActive()) {
            throw new RuntimeException("Inactive user cannot perform actions");
        }
        User existingUser = userRepository.findById(userID);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }
        if (newUserName != null) {
            User existing = userRepository.findByUserName(newUserName);
            if (existing != null && !existing.getUserID().equals(userID)) {
                throw new RuntimeException("Username already taken");
            }
        }
        String updatedName = (newUserName != null)
                ? newUserName
                : existingUser.getUserName();
        Role updatedRole = (newRole != null)
                ? newRole
                : existingUser.getRole();
        User updatedUser = new User(
                userID,
                updatedName,
                existingUser.getPassword(),
                updatedRole,
                existingUser.isActive());
        userRepository.save(userID, updatedUser);
    }
}
