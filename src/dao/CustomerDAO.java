package dao;

import model.Customer;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, first_name, last_name, address, email, phone, employment_status, company_name, company_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        System.out.println("Adding customer with ID: " + customer.getCustomerId());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customer.getCustomerId());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setString(4, customer.getAddress());
            stmt.setString(5, customer.getEmail());
            stmt.setString(6, customer.getPhone());
            stmt.setString(7, customer.getEmploymentStatus());
            stmt.setString(8, customer.getCompanyName());
            stmt.setString(9, customer.getCompanyAddress());

            int result = stmt.executeUpdate();
            System.out.println("✅ Customer added to database, affected rows: " + result);
            return result > 0;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in addCustomer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        System.out.println("Deleting customer with ID: " + customerId);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            int result = stmt.executeUpdate();

            if (result > 0) {

                UserDAO userDAO = new UserDAO();
                userDAO.deleteUser(customerId);
            }

            System.out.println("Deleted customer record, affected rows: " + result);
            return result > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error in deleteCustomer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Customer getCustomerById(int customerId) {
        System.out.println("Getting customer by ID: " + customerId);


        UserDAO userDAO = new UserDAO();
        boolean hasIsActive = userDAO.columnExists("users", "is_active");

        String sql;
        if (hasIsActive) {
            sql = "SELECT c.*, u.username FROM customers c JOIN users u ON c.customer_id = u.user_id WHERE c.customer_id = ? AND u.is_active = 1";
        } else {
            sql = "SELECT c.*, u.username FROM customers c JOIN users u ON c.customer_id = u.user_id WHERE c.customer_id = ?";
        }

        Customer customer = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setAddress(rs.getString("address"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setEmploymentStatus(rs.getString("employment_status"));
                customer.setCompanyName(rs.getString("company_name"));
                customer.setCompanyAddress(rs.getString("company_address"));
                System.out.println("✅ Found customer: " + customer.getFirstName() + " " + customer.getLastName());
            } else {
                System.out.println("❌ No customer found with ID: " + customerId);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getCustomerById: " + e.getMessage());
            e.printStackTrace();
        }
        return customer;
    }

    public List<Customer> getAllCustomers() {
        System.out.println("Getting all customers...");


        UserDAO userDAO = new UserDAO();
        boolean hasIsActive = userDAO.columnExists("users", "is_active");

        String sql;
        if (hasIsActive) {
            sql = "SELECT c.*, u.username FROM customers c JOIN users u ON c.customer_id = u.user_id WHERE u.is_active = 1 ORDER BY c.customer_id";
        } else {
            sql = "SELECT c.*, u.username FROM customers c JOIN users u ON c.customer_id = u.user_id ORDER BY c.customer_id";
        }

        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setAddress(rs.getString("address"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setEmploymentStatus(rs.getString("employment_status"));
                customer.setCompanyName(rs.getString("company_name"));
                customer.setCompanyAddress(rs.getString("company_address"));

                customers.add(customer);
            }
            System.out.println("✅ Found " + customers.size() + " customers");
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllCustomers: " + e.getMessage());
            e.printStackTrace();
        }
        return customers;
    }

    public List<Customer> searchCustomersByName(String name) {

        UserDAO userDAO = new UserDAO();
        boolean hasIsActive = userDAO.columnExists("users", "is_active");

        String sql;
        if (hasIsActive) {
            sql = "SELECT c.*, u.username FROM customers c JOIN users u ON c.customer_id = u.user_id WHERE (c.first_name LIKE ? OR c.last_name LIKE ?) AND u.is_active = 1 ORDER BY c.customer_id";
        } else {
            sql = "SELECT c.*, u.username FROM customers c JOIN users u ON c.customer_id = u.user_id WHERE (c.first_name LIKE ? OR c.last_name LIKE ?) ORDER BY c.customer_id";
        }

        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            stmt.setString(2, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setAddress(rs.getString("address"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setEmploymentStatus(rs.getString("employment_status"));
                customer.setCompanyName(rs.getString("company_name"));
                customer.setCompanyAddress(rs.getString("company_address"));

                customers.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in searchCustomersByName: " + e.getMessage());
            e.printStackTrace();
        }
        return customers;
    }

    public int getTotalCustomersCount() {

        UserDAO userDAO = new UserDAO();
        boolean hasIsActive = userDAO.columnExists("users", "is_active");

        String sql;
        if (hasIsActive) {
            sql = "SELECT COUNT(*) FROM customers c JOIN users u ON c.customer_id = u.user_id WHERE u.is_active = 1";
        } else {
            sql = "SELECT COUNT(*) FROM customers c JOIN users u ON c.customer_id = u.user_id";
        }

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getTotalCustomersCount: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}