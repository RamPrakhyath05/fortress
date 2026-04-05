package com.fortress.model;

public class User {

    private String userID;
    private String userName;
    private String password;
    private Role role;

    public User(String userID, String userName, String password, Role role) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
