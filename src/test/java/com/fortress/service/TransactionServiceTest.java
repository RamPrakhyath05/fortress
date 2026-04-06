package com.fortress.service;

import com.fortress.model.*;
import com.fortress.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceTest {

    private TransactionService transactionService;
    private UserRepository userRepository;

    @BeforeEach
    void setup() throws Exception {
        java.nio.file.Files.deleteIfExists(
                java.nio.file.Paths.get("fortress.db"));

        userRepository = new UserRepository();
        TransactionRepository tr = new TransactionRepository();

        transactionService = new TransactionService(tr, userRepository);

        userRepository.save("1", new User("1", "admin", "pass", Role.ADMIN, true));
        userRepository.save("2", new User("2", "viewer", "pass", Role.VIEWER, true));
    }

    @Test
    void shouldAddTransactionSuccessfully() {
        transactionService.addTransaction(
                "1", 1000.0, TransactionType.INCOME,
                "Salary", LocalDate.now(), "test", "1");

        List<Transaction> list = transactionService.getTransactionsByUser("1");

        assertEquals(1, list.size());
    }

    @Test
    void shouldBlockViewerFromAddingTransaction() {
        assertThrows(RuntimeException.class, () -> {
            transactionService.addTransaction(
                    "2", 500.0, TransactionType.EXPENSE,
                    "Food", LocalDate.now(), "test", "2");
        });
    }

    @Test
    void shouldUpdateTransaction() {
        transactionService.addTransaction(
                "1", 1000.0, TransactionType.INCOME,
                "Salary", LocalDate.now(), "test", "1");

        String id = transactionService.getTransactionsByUser("1")
                .get(0).getTransactionID();

        transactionService.updateTransaction(
                id, 2000.0, null,
                null, null, null, "1");

        Transaction t = transactionService.getTransactionById(id);

        assertEquals(2000.0, t.getAmount());
    }

    @Test
    void shouldDeleteTransactionOnlyByAdmin() {
        transactionService.addTransaction(
                "1", 1000.0, TransactionType.INCOME,
                "Salary", LocalDate.now(), "test", "1");

        String id = transactionService.getTransactionsByUser("1")
                .get(0).getTransactionID();

        assertThrows(RuntimeException.class, () -> {
            transactionService.deleteTransaction(id, "2"); // viewer
        });
    }

    @Test
    void shouldFilterTransactionsByType() {
        transactionService.addTransaction(
                "1", 1000.0, TransactionType.INCOME,
                "Salary", LocalDate.now(), "test", "1");

        transactionService.addTransaction(
                "1", 500.0, TransactionType.EXPENSE,
                "Food", LocalDate.now(), "test", "1");

        List<Transaction> expenses = transactionService.getFilteredTransactions(
                "1", TransactionType.EXPENSE, null, null, null);

        assertEquals(1, expenses.size());
    }

    @Test
    void shouldCalculateTotalIncome() {
        transactionService.addTransaction(
                "1", 1000.0, TransactionType.INCOME,
                "Salary", LocalDate.now(), "test", "1");

        double income = transactionService.getTotalIncome("1");

        assertEquals(1000.0, income);
    }
}
