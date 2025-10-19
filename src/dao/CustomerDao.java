package dao;

import model.Customer;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    // CREATE
    public void saveCustomer(Customer c) throws SQLException {
        String sql = "INSERT INTO customers (customerId, firstName, surName, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getCustomerId());
            stmt.setString(2, c.getFirstName());
            stmt.setString(3, c.getSurName());
            stmt.setString(4, c.getAddress());
            stmt.executeUpdate();
        }
    }

    // READ - all customers
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Customer(
                        rs.getString("customerId"),
                        rs.getString("firstName"),
                        rs.getString("surName"),
                        rs.getString("address")));
            }
        }
        return list;
    }

    // READ - by ID
    public Customer getCustomerById(String id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customerId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getString("customerId"),
                        rs.getString("firstName"),
                        rs.getString("surName"),
                        rs.getString("address"));
            }
        }
        return null;
    }

    // UPDATE
    public boolean updateCustomer(Customer c) throws SQLException {
        String sql = "UPDATE customers SET firstName = ?, surName = ?, address = ? WHERE customerId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getFirstName());
            stmt.setString(2, c.getSurName());
            stmt.setString(3, c.getAddress());
            stmt.setString(4, c.getCustomerId());
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    // DELETE
    public boolean deleteCustomer(String id) throws SQLException {
        String sql = "DELETE FROM customers WHERE customerId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
}
