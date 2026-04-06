package com.fortress.controller;

import com.fortress.service.UserService;
import com.fortress.model.*;
import com.fortress.exception.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // View all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Create user
    @PostMapping
    public String createUser(@RequestBody UserRequest request) {
        Role role = null;
        if (request.getRole() != null) {
            try {
                role = Role.valueOf(request.getRole().toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("Invalid role");
            }
        }
        userService.addUser(
                request.getUserName(),
                request.getPassword(),
                role);
        return "User created successfully";
    }

    // Get User by ID
    @GetMapping("/{id}")
    public User getUserDetails(@PathVariable String id) {
        return userService.getUserDetails(id);
    }

    // Verify User login
    @PostMapping("/login")
    public User login(@RequestBody UserRequest request) {
        return userService.verifyPassword(
                request.getUserName(),
                request.getPassword());

    }

    // Update User Status
    @PutMapping("/{id}/status")
    public String updateUserStatus(@PathVariable String id, @RequestParam boolean isActive,
            @RequestParam String modifierID) {
        userService.toggleUserStatus(id, isActive, modifierID);
        return "User status updated successfully";
    }

    // Update User by ID
    @PutMapping("/{id}")
    public String updateUser(@PathVariable String id, @RequestBody UserRequest request,
            @RequestParam String modifierID) {
        Role role = null;
        if (request.getRole() != null) {
            try {
                role = Role.valueOf(request.getRole().toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("Invalid role");
            }
        }
        userService.updateUser(
                id,
                request.getUserName(),
                role,
                modifierID);
        return "User details updated successfully.";
    }

    // Delete User by ID
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id, @RequestParam String modifierID) {
        userService.deleteUser(id, modifierID);
        return "User Deleted Successfully.";
    }
}
