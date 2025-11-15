package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:banking_system.db";

    static {
        initializeDatabase();
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
            System.out.println("✅ Connected to SQLite database successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    private static void initializeDatabase() {
        System.out.println("=== INITIALIZING DATABASE ===");
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {


            stmt.execute("PRAGMA foreign_keys = ON");


            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    user_type TEXT NOT NULL,
                    email TEXT,
                    is_active INTEGER DEFAULT 1,
                    created_date DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """;

            // Create customers table
            String createCustomersTable = """
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id INTEGER PRIMARY KEY,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    address TEXT,
                    email TEXT,
                    phone TEXT,
                    employment_status TEXT,
                    company_name TEXT,
                    company_address TEXT,
                    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (customer_id) REFERENCES users(user_id) ON DELETE CASCADE
                )
            """;

            // Create employees table
            String createEmployeesTable = """
                CREATE TABLE IF NOT EXISTS employees (
                    employee_id INTEGER PRIMARY KEY,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    department TEXT,
                    position TEXT,
                    hire_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (employee_id) REFERENCES users(user_id) ON DELETE CASCADE
                )
            """;

            // Create accounts table
            String createAccountsTable = """
                CREATE TABLE IF NOT EXISTS accounts (
                    account_number TEXT PRIMARY KEY,
                    customer_id INTEGER NOT NULL,
                    account_type TEXT NOT NULL,
                    balance REAL DEFAULT 0.00,
                    branch TEXT,
                    interest_rate REAL,
                    is_active INTEGER DEFAULT 1,
                    opened_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                    opened_by INTEGER,
                    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
                    FOREIGN KEY (opened_by) REFERENCES employees(employee_id)
                )
            """;

            // Create transactions table
            String createTransactionsTable = """
                CREATE TABLE IF NOT EXISTS transactions (
                    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    account_number TEXT NOT NULL,
                    transaction_type TEXT NOT NULL,
                    amount REAL NOT NULL,
                    description TEXT,
                    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                    performed_by INTEGER,
                    FOREIGN KEY (account_number) REFERENCES accounts(account_number),
                    FOREIGN KEY (performed_by) REFERENCES employees(employee_id)
                )
            """;

            // Execute all table creation statements
            stmt.execute(createUsersTable);
            stmt.execute(createCustomersTable);
            stmt.execute(createEmployeesTable);
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionsTable);

            System.out.println("✅ All tables created successfully!");

            // Insert default admin user
            insertSampleData(stmt);

        } catch (SQLException e) {
            System.err.println("❌ Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertSampleData(Statement stmt) throws SQLException {
        System.out.println("Inserting sample data...");

        // Insert default admin user
        String insertAdmin = """
            INSERT OR IGNORE INTO users (user_id, username, password, user_type, email) 
            VALUES (1, 'admin', 'admin123', 'ADMIN', 'admin@ebmbank.com')
        """;

        // Insert default employee user
        String insertEmployee = """
            INSERT OR IGNORE INTO users (user_id, username, password, user_type, email) 
            VALUES (2, 'employee1', 'emp123', 'EMPLOYEE', 'employee@ebmbank.com')
        """;

        // Insert employee details
        String insertEmployeeDetails = """
            INSERT OR IGNORE INTO employees (employee_id, first_name, last_name, department, position) 
            VALUES (2, 'John', 'Smith', 'Customer Service', 'Bank Teller')
        """;

        // Insert default customer user
        String insertCustomer = """
            INSERT OR IGNORE INTO users (user_id, username, password, user_type, email) 
            VALUES (3, 'customer1', 'cust123', 'CUSTOMER', 'customer@email.com')
        """;

        // Insert customer details
        String insertCustomerDetails = """
            INSERT OR IGNORE INTO customers (customer_id, first_name, last_name, email, phone, employment_status) 
            VALUES (3, 'Alice', 'Johnson', 'alice@email.com', '1234567890', 'Employed')
        """;

        // Insert sample accounts
        String insertSavingsAccount = """
            INSERT OR IGNORE INTO accounts (account_number, customer_id, account_type, balance, branch, interest_rate) 
            VALUES ('SAV-123456', 3, 'Savings', 5000.00, 'Main Branch', 0.0005)
        """;

        String insertChequeAccount = """
            INSERT OR IGNORE INTO accounts (account_number, customer_id, account_type, balance, branch, interest_rate) 
            VALUES ('CHQ-789012', 3, 'Cheque', 2500.00, 'Main Branch', 0.0)
        """;

        String insertInvestmentAccount = """
            INSERT OR IGNORE INTO accounts (account_number, customer_id, account_type, balance, branch, interest_rate) 
            VALUES ('INV-345678', 3, 'Investment', 10000.00, 'Main Branch', 0.05)
        """;

        // Insert sample transactions
        String insertTransaction1 = """
            INSERT OR IGNORE INTO transactions (account_number, transaction_type, amount, description, performed_by)
            VALUES ('SAV-123456', 'DEPOSIT', 5000.00, 'Initial account opening deposit', 2)
        """;

        String insertTransaction2 = """
            INSERT OR IGNORE INTO transactions (account_number, transaction_type, amount, description, performed_by)
            VALUES ('CHQ-789012', 'DEPOSIT', 2500.00, 'Initial account opening deposit', 2)
        """;

        String insertTransaction3 = """
            INSERT OR IGNORE INTO transactions (account_number, transaction_type, amount, description, performed_by)
            VALUES ('INV-345678', 'DEPOSIT', 10000.00, 'Initial account opening deposit', 2)
        """;

        // Execute insert statements
        stmt.execute(insertAdmin);
        stmt.execute(insertEmployee);
        stmt.execute(insertEmployeeDetails);
        stmt.execute(insertCustomer);
        stmt.execute(insertCustomerDetails);
        stmt.execute(insertSavingsAccount);
        stmt.execute(insertChequeAccount);
        stmt.execute(insertInvestmentAccount);
        stmt.execute(insertTransaction1);
        stmt.execute(insertTransaction2);
        stmt.execute(insertTransaction3);

        System.out.println("✅ Sample data inserted successfully!");
    }

    // Test connection
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ SQLite database connection test: SUCCESS");
            System.out.println("📁 Database file: banking_system.db");
        } catch (SQLException e) {
            System.out.println("❌ SQLite database connection test: FAILED");
            e.printStackTrace();
        }
    }
}