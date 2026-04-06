package com.fortress.service;

import org.springframework.stereotype.Service;

import com.fortress.model.Role;
import com.fortress.model.User;
import com.fortress.repository.UserRepository;
import com.fortress.util.PasswordHasher;
import com.fortress.exception.*;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordHasher hasher;

    public UserService(UserRepository userRepository, PasswordHasher hasher) {
        this.userRepository = userRepository;
        this.hasher = hasher;
    }

    // To fetch user details using userID
    public User getUserDetails(String userID) {
        User user = userRepository.findById(userID);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    // To verify login credentials of a user
    public User verifyPassword(String userName, String userPassword) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (!user.isActive()) {
            throw new UnauthorizedException("User is inactive");
        }
        String hashedInput = hasher.getHashedPassword(userPassword);
        if (!hashedInput.equals(user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }
        return user;
    }

    // To get all users from the database
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    // To create a new user after checking username uniqueness
    public void addUser(String userName, String userPassword, Role role) {
        User existing = userRepository.findByUserName(userName);
        if (existing != null) {
            throw new BadRequestException("Username already exists");
        }
        String userID = "user-" + UUID.randomUUID().toString().substring(0, 6);
        // Generating a short unique ID for the user
        String hashedPassword = hasher.getHashedPassword(userPassword);
        User user = new User(userID, userName, hashedPassword, role, true);
        userRepository.save(userID, user);
    }

    // To enable or disable a user account based on admin action
    public void toggleUserStatus(String userID, boolean isActive, String requesterID) {
        User modifier = userRepository.findById(requesterID);
        if (modifier == null) {
            throw new NotFoundException("Requester not found");
        }
        if (!modifier.isActive()) {
            throw new UnauthorizedException("Inactive user cannot perform actions");
        }
        if (modifier.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Access denied, ADMIN only");
        }
        User user = userRepository.findById(userID);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        User updated = new User(
                user.getUserID(),
                user.getUserName(),
                user.getPassword(),
                user.getRole(),
                isActive);
        userRepository.save(userID, updated);
    }

    // To delete a user after validating admin permissions
    public void deleteUser(String userID, String requesterID) {
        User modifier = userRepository.findById(requesterID);
        if (modifier == null) {
            throw new NotFoundException("Requester not found");
        }
        if (modifier.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Access denied, ADMIN only");
        }
        if (!modifier.isActive()) {
            throw new UnauthorizedException("Inactive user cannot perform actions");
        }
        User existing = userRepository.findById(userID);
        if (existing == null) {
            throw new NotFoundException("User not found");
        }
        userRepository.delete(userID);
    }

    // To update user details like username and role
    public void updateUser(String userID, String newUserName, Role newRole, String requesterID) {
        User modifier = userRepository.findById(requesterID);
        if (modifier == null) {
            throw new NotFoundException("Modifier not found");
        }
        if (modifier.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Access denied, ADMIN only");
        }
        if (!modifier.isActive()) {
            throw new UnauthorizedException("Inactive user cannot perform actions");
        }
        User existingUser = userRepository.findById(userID);
        if (existingUser == null) {
            throw new NotFoundException("User not found");
        }
        if (newUserName != null) {
            User existing = userRepository.findByUserName(newUserName);

            if (existing != null && !existing.getUserID().equals(userID)) {
                throw new BadRequestException("Username already taken");
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
