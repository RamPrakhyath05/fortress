package com.fortress.controller;

import com.fortress.model.Transaction;
import com.fortress.model.TransactionType;
import com.fortress.service.TransactionService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public String createTransaction(@RequestBody TransactionRequest request) {
        TransactionType type = TransactionType.valueOf(request.getType().toUpperCase());
        LocalDate date = LocalDate.parse(request.getDate());
        transactionService.addTransaction(
                request.getUserID(),
                request.getAmount(),
                type,
                request.getCategory(),
                date,
                request.getNotes()
        );
        return "Transaction created successfully";
    }

    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable String id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/user/{userID}")
    public List<Transaction> getUserTransactions(@PathVariable String userID) {
        return transactionService.getTransactionsByUser(userID);
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
        return "Transaction deleted";
    }
}
