package com.fortress.service;

import org.springframework.stereotype.Service;

import com.fortress.model.*;
import com.fortress.repository.TransactionRepository;
import com.fortress.repository.UserRepository;
import com.fortress.exception.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    // To create a new transaction after validating user and requester
    public void addTransaction(String userID, Double amount, TransactionType type,
            String category, LocalDate date, String notes, String requesterID) {
        User user = userRepository.findById(userID);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        User requester = userRepository.findById(requesterID);
        if (requester == null) {
            throw new NotFoundException("Requester not found");
        }
        if (!requester.isActive()) {
            throw new UnauthorizedException("Inactive user cannot perform actions");
        }
        if (requester.getRole() == Role.VIEWER) {
            throw new UnauthorizedException("Access denied: VIEWER cannot create transactions");
        }
        String transactionID = "transac-" + UUID.randomUUID().toString().substring(0, 8);
        // Now using an actual string ID instead of just an integer
        Transaction transaction = new Transaction(
                transactionID,
                userID,
                amount,
                type,
                category,
                date,
                notes);
        transactionRepository.save(transactionID, transaction);
    }

    // To fetch transaction details using transactionID
    public Transaction getTransactionById(String transactionID) {
        Transaction transaction = transactionRepository.findByTransactionID(transactionID);
        if (transaction == null) {
            throw new NotFoundException("Transaction not found");
        }
        return transaction;
    }

    // To fetch all transactions of a specific user
    public List<Transaction> getTransactionsByUser(String userID) {
        return transactionRepository.findByUserID(userID);
    }

    // To delete a transaction after validating requester permissions
    public void deleteTransaction(String transactionID, String requesterID) {
        Transaction existing = transactionRepository.findByTransactionID(transactionID);
        if (existing == null) {
            throw new NotFoundException("Transaction not found");
        }
        User requester = userRepository.findById(requesterID);
        if (requester == null) {
            throw new NotFoundException("Requester not found");
        }
        if (!requester.isActive()) {
            throw new UnauthorizedException("Inactive user cannot perform actions");
        }
        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only ADMIN can delete transactions");
        }
        transactionRepository.delete(transactionID);
    }

    // To update transaction details with partial update support
    public void updateTransaction(
            String transactionID,
            Double amount,
            TransactionType type,
            String category,
            LocalDate date,
            String notes,
            String requesterID) {
        User requester = userRepository.findById(requesterID);
        if (requester == null) {
            throw new NotFoundException("Requester not found");
        }
        if (!requester.isActive()) {
            throw new UnauthorizedException("Inactive user cannot perform actions");
        }
        if (requester.getRole() == Role.VIEWER) {
            throw new UnauthorizedException("Access denied: VIEWER cannot modify transactions");
        }
        Transaction existing = transactionRepository.findByTransactionID(transactionID);
        if (existing == null) {
            throw new NotFoundException("Transaction not found");
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
                updatedNotes);
        transactionRepository.save(transactionID, updated);
    }

    /* Analytics functions */
    // To filter transactions based on optional parameters like type category and
    // date range
    public List<Transaction> getFilteredTransactions(
            String userID,
            TransactionType type,
            String category,
            LocalDate startDate,
            LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByUserID(userID);
        return transactions.stream().filter(t -> type == null || t.getType() == type)
                .filter(t -> category == null || t.getCategory().equalsIgnoreCase(category))
                .filter(t -> startDate == null || !t.getDate().isBefore(startDate))
                .filter(t -> endDate == null || !t.getDate().isAfter(endDate))
                .toList();
    }

    // To calculate total income for a user
    public Double getTotalIncome(String userID) {
        return transactionRepository.findByUserID(userID).stream().filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // To calculate total expense for a user
    public Double getTotalExpense(String userID) {
        return transactionRepository.findByUserID(userID).stream().filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // To calculate net balance
    public Double getNetBalance(String userID) {
        return getTotalIncome(userID) - getTotalExpense(userID);
    }

    // To get expense breakdown category wise
    public Map<String, Double> getCategoryBreakdown(String userID) {
        Map<String, Double> result = new HashMap<>();
        for (Transaction t : transactionRepository.findByUserID(userID)) {
            if (t.getType() == TransactionType.EXPENSE) {
                result.put(
                        t.getCategory(),
                        result.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            }
        }
        return result;
    }

    // To calculate monthly expense trends
    public Map<String, Double> getMonthlyExpenseTrends(String userID) {
        List<Transaction> transactions = transactionRepository.findByUserID(userID);
        Map<String, Double> trends = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE) {
                String month = t.getDate().getYear() + "-" +
                        String.format("%02d", t.getDate().getMonthValue());

                trends.put(
                        month,
                        trends.getOrDefault(month, 0.0) + t.getAmount());
            }
        }
        return trends;
    }
}
