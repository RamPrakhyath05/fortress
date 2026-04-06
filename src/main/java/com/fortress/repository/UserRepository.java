package com.fortress.repository;

import org.springframework.stereotype.Repository;

import com.fortress.model.Role;
import com.fortress.model.User;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

@Repository
// New implementation - using SQLite database instead of in-memory HashMap
public class UserRepository {
    // private HashMap<String, User> users = new HashMap<>();
    private final Connection connection;

    public UserRepository() { // Constructor
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:fortress.db");
            // To create a connection to the sqlite database
            Statement stmt = connection.createStatement();
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "userID TEXT PRIMARY KEY," +
                            "userName TEXT UNIQUE," +
                            "password TEXT," +
                            "role TEXT," +
                            "isActive BOOLEAN)");
            // This will create a table if it doesnt exist
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // To get all the existing records in the users table
    public List<User> getAllUsers() {
        // return users.values();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM users");
                ResultSet rs = ps.executeQuery()) {
            // PreparedStatement is used when we need some DML query to run, like INSERT,
            // UPDATE etc.
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(map(rs));
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // To add or update an existing record in the Users table
    public void save(String userID, User user) {
        // users.put(userID, user);
        try (PreparedStatement ps = connection
                .prepareStatement("INSERT OR REPLACE INTO users VALUES (?, ?, ?, ?, ?)");) {
            ps.setString(1, user.getUserID());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole().name());
            ps.setBoolean(5, user.isActive());
            // mapping all the variables to the query param :D
            ps.executeUpdate(); // to execute the query / start the transaction
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Delete record in Users table based on userID
    public void delete(String userID) {
        // users.remove(userID);
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE userID = ?")) {
            ps.setString(1, userID);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Find user details by userID
    public User findById(String userID) {
        // return users.get(userID);
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE userID = ?")) {
            ps.setString(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                // Same as executeUpdate() but this will return something and we're storing it
                // in rs
                if (rs.next()) {
                    return map(rs);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User findByUserName(String userName) {
        /*
         * for (User user : users.values()) {
         * if (user.getUserName().equals(userName)) {
         * return user;
         * }
         * }
         * return null;
         */
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE userName = ?")) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // This is a method so that the data from the database can be converted into the
    // User Object
    private User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("userID"),
                rs.getString("userName"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role")),
                rs.getBoolean("isActive"));
    }
}
