package com.fortress.repository;

import org.springframework.stereotype.Repository;

import com.fortress.model.Transaction;
import com.fortress.model.TransactionType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
// New implementation - using SQLite database instead of in-memory HashMap
public class TransactionRepository {
    // private HashMap<String, Transaction> transactions = new HashMap<>();
    private final Connection connection;

    public TransactionRepository() { // Constructor
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:fortress.db");
            // To create a connection to the sqlite database
            Statement stmt = connection.createStatement();
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS transactions (" +
                            "transactionID TEXT PRIMARY KEY," +
                            "userID TEXT," +
                            "amount REAL," +
                            "type TEXT," +
                            "category TEXT," +
                            "date TEXT," +
                            "notes TEXT)");
            // This will create a table if it doesnt exist
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // To add or update an existing record in the Transactions table
    public void save(String transactionID, Transaction transaction) {
        // transactions.put(transactionID, transaction);
        try (PreparedStatement ps = connection
                .prepareStatement("INSERT OR REPLACE INTO transactions VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            // PreparedStatement is used when we need some DML query to run, like INSERT,
            // UPDATE etc.
            ps.setString(1, transaction.getTransactionID());
            ps.setString(2, transaction.getUserID());
            ps.setDouble(3, transaction.getAmount());
            ps.setString(4, transaction.getType().name());
            ps.setString(5, transaction.getCategory());
            ps.setString(6, transaction.getDate().toString());
            ps.setString(7, transaction.getNotes());
            // mapping all the variables to the query param :D
            ps.executeUpdate(); // to execute the query / start the transaction
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Delete record in Transactions table based on transactionID
    public void delete(String transactionID) {
        // transactions.remove(transactionID);
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM transactions WHERE transactionID = ?")) {
            ps.setString(1, transactionID);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Find transaction details by transactionID
    public Transaction findByTransactionID(String transactionID) {
        // return transactions.get(transactionID);
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM transactions WHERE transactionID = ?")) {
            ps.setString(1, transactionID);

            try (ResultSet rs = ps.executeQuery()) {
                // Same as executeUpdate() but this will return something and we're storing it
                // in rs
                if (rs.next()) {
                    return map(rs);
                }
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Transaction> findByUserID(String userID) {
        /*
         * List<Transaction> result = new ArrayList<>();
         * for (Transaction t : transactions.values()) {
         * if (t.getUserID().equals(userID)) {
         * result.add(t);
         * }
         * }
         * return result;
         */
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM transactions WHERE userID = ?")) {
            ps.setString(1, userID);

            try (ResultSet rs = ps.executeQuery()) {
                List<Transaction> result = new ArrayList<>();

                while (rs.next()) {
                    result.add(map(rs));
                }

                return result;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // This is a method so that the data from the database can be converted into the
    // Transaction Object
    private Transaction map(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getString("transactionID"),
                rs.getString("userID"),
                rs.getDouble("amount"),
                TransactionType.valueOf(rs.getString("type")),
                rs.getString("category"),
                LocalDate.parse(rs.getString("date")),
                rs.getString("notes"));
    }
}
