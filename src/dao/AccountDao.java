package dao;

import model.Account;
import model.Customer;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    // CREATE
    public void saveAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (accountNumber, balance, branch, ownerId) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getAccountNumber());
            stmt.setDouble(2, account.getBalance());
            stmt.setString(3, account.getBranch());
            stmt.setString(4, account.getOwner().getCustomerId());
            stmt.executeUpdate();
        }
    }

    // READ - all accounts
    public List<Account> getAllAccounts() throws SQLException {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer owner = new Customer(rs.getString("ownerId"), "", "", "");
                list.add(new Account(
                        rs.getString("accountNumber"),
                        rs.getDouble("balance"),
                        rs.getString("branch"),
                        owner));
            }
        }
        return list;
    }

    // READ - by Account Number
    public Account getAccountByNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE accountNumber = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Customer owner = new Customer(rs.getString("ownerId"), "", "", "");
                return new Account(
                        rs.getString("accountNumber"),
                        rs.getDouble("balance"),
                        rs.getString("branch"),
                        owner);
            }
        }
        return null;
    }

    // UPDATE
    public boolean updateAccountBalance(String accountNumber, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE accountNumber = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    // DELETE
    public boolean deleteAccount(String accountNumber) throws SQLException {
        String sql = "DELETE FROM accounts WHERE accountNumber = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
}
