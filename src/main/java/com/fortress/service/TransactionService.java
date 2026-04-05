package com.fortress.service;

import org.springframework.stereotype.Service;

import com.fortress.model.Transaction;
import com.fortress.model.TransactionType;
import com.fortress.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private int idCounter = 1;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void addTransaction(String userID, Double amount, TransactionType type,
                               String category, LocalDate date, String notes) {

        String transactionID = String.valueOf(idCounter++);
        Transaction transaction = new Transaction(
                transactionID,
                userID,
                amount,
                type,
                category,
                date,
                notes
        );
        transactionRepository.save(transactionID, transaction);
    }

    public Transaction getTransactionById(String transactionID) {
        return transactionRepository.findByTransactionID(transactionID);
    }

    public List<Transaction> getTransactionsByUser(String userID) {
        return transactionRepository.findByUserID(userID);
    }

    public void deleteTransaction(String transactionID) {
        transactionRepository.delete(transactionID);
    }
}
