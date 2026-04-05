package com.fortress.controller;

import com.fortress.service.UserService;
import com.fortress.model.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String createUser(@RequestBody UserRequest request) {

        Role role = Role.valueOf(request.getRole().toUpperCase());

        userService.addUser(
                request.getUserName(),
                request.getPassword(),
                role
        );

        return "User created successfully";
    }

    @GetMapping("/{id}")
    public User getUserDetails(@PathVariable String id){
        return userService.getUserDetails(id);
    }
}
