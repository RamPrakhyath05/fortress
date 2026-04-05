package com.fortress.controller;

public class TransactionRequest {

    private String userID;
    private Double amount;
    private String type;
    private String category;
    private String date;
    private String notes;

    public String getUserID() {
        return userID;
    }

    public Double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }
}
