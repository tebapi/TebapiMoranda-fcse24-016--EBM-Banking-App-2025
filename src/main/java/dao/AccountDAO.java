package dao;

import model.*;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public boolean addAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, interest_rate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getAccountNumber());
            stmt.setInt(2, account.getOwner().getCustomerId());
            stmt.setString(3, account.getAccountType());
            stmt.setDouble(4, account.getBalance());
            stmt.setString(5, account.getBranch());
            stmt.setDouble(6, account.getInterestRate());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error in addAccount: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Account> getAccountsByCustomerId(int customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.*, c.first_name, c.last_name, c.email FROM accounts a " +
                "JOIN customers c ON a.customer_id = c.customer_id " +
                "WHERE a.customer_id = ? AND a.is_active = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);  // This sets the customer ID
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));

                Account account = createAccountFromResultSet(rs, customer);
                if (account != null) {
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAccountsByCustomerId: " + e.getMessage());
            e.printStackTrace();
        }
        return accounts;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.*, c.first_name, c.last_name, c.email FROM accounts a " +
                "JOIN customers c ON a.customer_id = c.customer_id " +
                "WHERE a.is_active = 1 ORDER BY a.opened_date DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));

                Account account = createAccountFromResultSet(rs, customer);
                if (account != null) {
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllAccounts: " + e.getMessage());
            e.printStackTrace();
        }
        return accounts;
    }

    private Account createAccountFromResultSet(ResultSet rs, Customer customer) throws SQLException {
        String accountType = rs.getString("account_type");
        String accountNumber = rs.getString("account_number");
        double balance = rs.getDouble("balance");
        String branch = rs.getString("branch");

        Account account = null;
        switch (accountType) {
            case "Savings":
                account = new SavingsAccount(accountNumber, balance, branch, customer);
                break;
            case "Investment":
                account = new InvestmentAccount(accountNumber, balance, branch, customer);
                break;
            case "Cheque":
                account = new ChequeAccount(accountNumber, balance, branch, customer);
                break;
            default:
                return null;
        }

        account.setInterestRate(rs.getDouble("interest_rate"));
        return account;
    }

    public boolean updateAccountBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error in updateAccountBalance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Account getAccountByNumber(String accountNumber) {
        String sql = "SELECT a.*, c.first_name, c.last_name, c.email FROM accounts a " +
                "JOIN customers c ON a.customer_id = c.customer_id " +
                "WHERE a.account_number = ? AND a.is_active = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));

                return createAccountFromResultSet(rs, customer);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAccountByNumber: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}