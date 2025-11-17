package dao;

import model.Employee;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (employee_id, first_name, last_name, department, position) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employee.getEmployeeId());
            stmt.setString(2, employee.getFirstName());
            stmt.setString(3, employee.getLastName());
            stmt.setString(4, employee.getDepartment());
            stmt.setString(5, employee.getPosition());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error in addEmployee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Employee getEmployeeById(int employeeId) {

        UserDAO userDAO = new UserDAO();
        boolean hasIsActive = userDAO.columnExists("users", "is_active");

        String sql;
        if (hasIsActive) {
            sql = "SELECT e.*, u.username, u.email FROM employees e JOIN users u ON e.employee_id = u.user_id WHERE e.employee_id = ? AND u.is_active = 1";
        } else {
            sql = "SELECT e.*, u.username, u.email FROM employees e JOIN users u ON e.employee_id = u.user_id WHERE e.employee_id = ?";
        }

        Employee employee = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                employee = new Employee();
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setLastName(rs.getString("last_name"));
                employee.setDepartment(rs.getString("department"));
                employee.setPosition(rs.getString("position"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getEmployeeById: " + e.getMessage());
            e.printStackTrace();
        }
        return employee;
    }

    public List<Employee> getAllEmployees() {

        UserDAO userDAO = new UserDAO();
        boolean hasIsActive = userDAO.columnExists("users", "is_active");

        String sql;
        if (hasIsActive) {
            sql = "SELECT e.*, u.username, u.email FROM employees e JOIN users u ON e.employee_id = u.user_id WHERE u.is_active = 1 ORDER BY e.employee_id";
        } else {
            sql = "SELECT e.*, u.username, u.email FROM employees e JOIN users u ON e.employee_id = u.user_id ORDER BY e.employee_id";
        }

        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setLastName(rs.getString("last_name"));
                employee.setDepartment(rs.getString("department"));
                employee.setPosition(rs.getString("position"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllEmployees: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }

    public int getTotalEmployeesCount() {

        UserDAO userDAO = new UserDAO();
        boolean hasIsActive = userDAO.columnExists("users", "is_active");

        String sql;
        if (hasIsActive) {
            sql = "SELECT COUNT(*) FROM employees e JOIN users u ON e.employee_id = u.user_id WHERE u.is_active = 1";
        } else {
            sql = "SELECT COUNT(*) FROM employees e JOIN users u ON e.employee_id = u.user_id";
        }

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getTotalEmployeesCount: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public boolean deleteEmployee(int employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            int result = stmt.executeUpdate();

            if (result > 0) {

                UserDAO userDAO = new UserDAO();
                userDAO.deleteUser(employeeId);
            }

            return result > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error in deleteEmployee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}