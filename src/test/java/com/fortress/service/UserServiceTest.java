package com.fortress.service;

import com.fortress.model.*;
import com.fortress.repository.UserRepository;
import com.fortress.util.PasswordHasher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setup() throws Exception {
        java.nio.file.Files.deleteIfExists(
                java.nio.file.Paths.get("fortress.db"));

        userService = new UserService(new UserRepository(), new PasswordHasher());
    }

    @Test
    void shouldCreateUserSuccessfully() {
        userService.addUser("ram", "123", Role.ADMIN);

        User user = userService.verifyPassword("ram", "123");

        assertNotNull(user);
        assertEquals("ram", user.getUserName());
    }

    @Test
    void shouldNotAllowDuplicateUsername() {
        userService.addUser("ram", "123", Role.ADMIN);

        Exception ex = assertThrows(RuntimeException.class, () -> {
            userService.addUser("ram", "456", Role.VIEWER);
        });

        assertTrue(ex.getMessage().contains("Username already exists"));
    }

    @Test
    void shouldFailLoginWithWrongPassword() {
        userService.addUser("ram", "123", Role.ADMIN);

        assertThrows(RuntimeException.class, () -> {
            userService.verifyPassword("ram", "wrong");
        });
    }

    @Test
    void shouldBlockInactiveUserLogin() {
        userService.addUser("ram", "123", Role.ADMIN);

        User user = userService.verifyPassword("ram", "123");
        String id = user.getUserID();

        userService.toggleUserStatus(id, false, id);

        assertThrows(RuntimeException.class, () -> {
            userService.verifyPassword("ram", "123");
        });
    }
}
