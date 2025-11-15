package dao;

import model.User;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User authenticate(String username, String password) {
        System.out.println("Authenticating user: " + username);


        if (!columnExists("users", "is_active")) {

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            return authenticateWithQuery(sql, username, password);
        } else {

            String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND is_active = 1";
            return authenticateWithQuery(sql, username, password);
        }
    }

    private User authenticateWithQuery(String sql, String username, String password) {
        User user = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            System.out.println("Executing query: " + sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setUserType(rs.getString("user_type"));
                user.setEmail(rs.getString("email"));
                System.out.println("✅ User authenticated: " + user.getUsername() + " (ID: " + user.getUserId() + ")");
            } else {
                System.out.println("❌ Authentication failed for: " + username);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in authenticate: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, user_type, email) VALUES (?, ?, ?, ?)";
        System.out.println("Registering user: " + user.getUsername());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getUserType());
            stmt.setString(4, user.getEmail());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                        System.out.println("✅ User registered with ID: " + user.getUserId());
                        return true;
                    }
                }
            }
            System.out.println("❌ User registration failed - no rows affected");
            return false;
        } catch (SQLException e) {
            System.err.println("SQL Error in registerUser: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        if (columnExists("users", "is_active")) {
            String sql = "UPDATE users SET is_active = 0 WHERE user_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, userId);
                int result = stmt.executeUpdate();
                System.out.println("Deactivated user record, affected rows: " + result);
                return result > 0;

            } catch (SQLException e) {
                System.err.println("SQL Error in deleteUser: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {

            String sql = "DELETE FROM users WHERE user_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, userId);
                int result = stmt.executeUpdate();
                System.out.println("Deleted user record, affected rows: " + result);
                return result > 0;

            } catch (SQLException e) {
                System.err.println("SQL Error in deleteUser: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean usernameExists(String username) {
        String sql;
        if (columnExists("users", "is_active")) {
            sql = "SELECT COUNT(*) FROM users WHERE username = ? AND is_active = 1";
        } else {
            sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean exists = rs.getInt(1) > 0;
                System.out.println("Username '" + username + "' exists: " + exists);
                return exists;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in usernameExists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql;
        if (columnExists("users", "is_active")) {
            sql = "SELECT * FROM users WHERE is_active = 1 ORDER BY user_id";
        } else {
            sql = "SELECT * FROM users ORDER BY user_id";
        }

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setUserType(rs.getString("user_type"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllUsers: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getUsersByType(String userType) {
        List<User> users = new ArrayList<>();
        String sql;
        if (columnExists("users", "is_active")) {
            sql = "SELECT * FROM users WHERE user_type = ? AND is_active = 1 ORDER BY user_id";
        } else {
            sql = "SELECT * FROM users WHERE user_type = ? ORDER BY user_id";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setUserType(rs.getString("user_type"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getUsersByType: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }


    public boolean columnExists(String tableName, String columnName) {

        String sql = "PRAGMA table_info(" + tableName + ")";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                if (columnName.equals(rs.getString("name"))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if column exists: " + e.getMessage());
        }
        return false;
    }
}