package com.fortress.model;

import java.time.LocalDate;

public class Transaction {
    private String transactionID;
    private String transactionUserID;
    private Double amount;
    private TransactionType type;
    private String category;
    private LocalDate date;
    private String notes;

    public Transaction(String transactionID, String transactionUserID, Double amount,
                       TransactionType type, String category,
                       LocalDate date, String notes) {
        this.transactionID = transactionID;
        this.transactionUserID = transactionUserID;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.notes = notes;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getUserID() {
        return transactionUserID;
    }

    public Double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }
}
