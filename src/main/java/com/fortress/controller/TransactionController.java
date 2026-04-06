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
    public String createTransaction(
            @RequestBody TransactionRequest request,
            @RequestParam String requesterID
    ) 
    {
        TransactionType type = TransactionType.valueOf(request.getType().toUpperCase());
        LocalDate date = LocalDate.parse(request.getDate());
        transactionService.addTransaction(
                request.getUserID(),
                request.getAmount(),
                type,
                request.getCategory(),
                date,
                request.getNotes(),
                requesterID
        );
        return "Transaction created successfully";
    }

    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable String id) {
        return transactionService.getTransactionById(id);
    }

    
    @GetMapping("/user/{userID}")
    public List<Transaction> getUserTransactions(
            @PathVariable String userID,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) 
    {
        TransactionType transactionType = (type != null) ? TransactionType.valueOf(type.toUpperCase()) : null;
        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;
        return transactionService.getFilteredTransactions(
                userID,
                transactionType,
                category,
                start,
                end
        );
    }

    @PutMapping("/{id}")
    public String updateTransaction(
            @PathVariable String id,
            @RequestBody TransactionRequest request,
            @RequestParam String requesterID
    ) 
    {

        TransactionType type = (request.getType() != null) ? TransactionType.valueOf(request.getType().toUpperCase()) : null;
        LocalDate date = (request.getDate() != null) ? LocalDate.parse(request.getDate()) : null;
        transactionService.updateTransaction(
                id,
                request.getAmount(),
                type,
                request.getCategory(),
                date,
                request.getNotes(),
                requesterID
        );
        return "Transaction updated successfully";
    }
    
    @DeleteMapping("/{id}")
    public String deleteTransaction(
            @PathVariable String id,
            @RequestParam String requesterID
    ) 
    {
        transactionService.deleteTransaction(id, requesterID);
        return "Transaction deleted successfully";
    }
}
