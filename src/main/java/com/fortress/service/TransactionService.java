package com.fortress.service;

import org.springframework.stereotype.Service;

import com.fortress.model.*;
import com.fortress.repository.TransactionRepository;
import com.fortress.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private int idCounter = 1;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public void addTransaction(String userID, Double amount, TransactionType type,
                               String category, LocalDate date, String notes, String requesterID) {
        User requester = userRepository.findById(requesterID);
        if (requester == null) {
            throw new RuntimeException("Requester not found");
        }
        if (!requester.isActive()) {
            throw new RuntimeException("Inactive user cannot perform actions");
        }
        if (requester.getRole() == Role.VIEWER) {
            throw new RuntimeException("Access denied: VIEWER cannot create transactions");
        }
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

    public void deleteTransaction(String transactionID, String requesterID) {
        User requester = userRepository.findById(requesterID);
        if (requester == null) {
            throw new RuntimeException("Requester not found");
        }
        if (!requester.isActive()) {
            throw new RuntimeException("Inactive user cannot perform actions");
        }
        if (requester.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can delete transactions");
        }
        transactionRepository.delete(transactionID);
    }

    public void updateTransaction(
            String transactionID,
            Double amount,
            TransactionType type,
            String category,
            LocalDate date,
            String notes,
            String requesterID
    ) 
    {
        User requester = userRepository.findById(requesterID);
        if (requester == null) {
            throw new RuntimeException("Requester not found");
        }
        if (!requester.isActive()) {
            throw new RuntimeException("Inactive user cannot perform actions");
        }
        if (requester.getRole() == Role.VIEWER) {
            throw new RuntimeException("Access denied: VIEWER cannot modify transactions");
        }
        Transaction existing = transactionRepository.findByTransactionID(transactionID);
        if (existing == null) {
            throw new RuntimeException("Transaction not found");
        }
        Double updatedAmount = (amount != null) ? amount : existing.getAmount();
        TransactionType updatedType = (type != null) ? type : existing.getType();
        String updatedCategory = (category != null) ? category : existing.getCategory();
        LocalDate updatedDate = (date != null) ? date : existing.getDate();
        String updatedNotes = (notes != null) ? notes : existing.getNotes();
        Transaction updated = new Transaction(
                transactionID,
                existing.getUserID(),
                updatedAmount,
                updatedType,
                updatedCategory,
                updatedDate,
                updatedNotes
        );
        transactionRepository.save(transactionID, updated);
    }

    public List<Transaction> getFilteredTransactions(
        String userID,
        TransactionType type,
        String category,
        LocalDate startDate,
        LocalDate endDate
    ) 
    {
        List<Transaction> transactions = transactionRepository.findByUserID(userID);
        return transactions.stream().filter(t -> type == null || t.getType() == type).filter(t -> category == null || t.getCategory().equalsIgnoreCase(category))
                .filter(t -> startDate == null || !t.getDate().isBefore(startDate))
                .filter(t -> endDate == null || !t.getDate().isAfter(endDate))
                .toList();
    }

    //Analytics functions
    public Double getTotalIncome(String userID){
         return transactionRepository.findByUserID(userID).stream().filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount).sum();
    }
    public Double getTotalExpense(String userID) {
        return transactionRepository.findByUserID(userID).stream().filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount).sum();
    }
    public Double getNetBalance(String userID) {
        return getTotalIncome(userID) - getTotalExpense(userID);
    }
    public Map<String, Double> getCategoryBreakdown(String userID) {
        Map<String, Double> result = new HashMap<>();
        for (Transaction t : transactionRepository.findByUserID(userID)) {
            if (t.getType() == TransactionType.EXPENSE) {
                result.put(
                    t.getCategory(),
                    result.getOrDefault(t.getCategory(), 0.0) + t.getAmount()
                );
            }
        }
        return result;
    }
}
