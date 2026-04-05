package com.fortress.repository;

import java.util.*;
import org.springframework.stereotype.Repository;
import com.fortress.model.Transaction;

@Repository
public class TransactionRepository {

    private HashMap<String, Transaction> transactions = new HashMap<>();

    public void save(String transactionID, Transaction transaction) {
        transactions.put(transactionID, transaction);
    }

    public void delete(String transactionID) {
        transactions.remove(transactionID);
    }

    public Transaction findByTransactionID(String transactionID) {
        return transactions.get(transactionID);
    }

    public List<Transaction> findByUserID(String userID) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions.values()) {
            if (t.getUserID().equals(userID)) {
                result.add(t);
            }
        }
        return result;
    }
}
