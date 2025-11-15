package controller;

import dao.AccountDAO;
import dao.UserDAO;
import dao.CustomerDAO;
import dao.EmployeeDAO;
import dao.TransactionDAO;
import model.Account;
import model.User;
import model.Customer;
import model.Employee;
import model.Transaction;

import java.util.List;

public class AdminController {
    private AccountDAO accountDAO;
    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;
    private TransactionDAO transactionDAO;

    public AdminController() {
        this.accountDAO = new AccountDAO();
        this.userDAO = new UserDAO();
        this.customerDAO = new CustomerDAO();
        this.employeeDAO = new EmployeeDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public void applyMonthlyInterest() {
        try {
            List<Account> allAccounts = accountDAO.getAllAccounts();
            int updatedCount = 0;

            for (Account account : allAccounts) {
                if (account.getInterestRate() > 0 && account.getBalance() > 0) {
                    double interest = account.getBalance() * account.getInterestRate();
                    double newBalance = account.getBalance() + interest;

                    if (accountDAO.updateAccountBalance(account.getAccountNumber(), newBalance)) {
                        // Record interest transaction
                        Transaction transaction = new Transaction(
                                account.getAccountNumber(),
                                "INTEREST",
                                interest,
                                "Monthly interest payment"
                        );
                        transactionDAO.addTransaction(transaction);
                        updatedCount++;
                    }
                }
            }

            System.out.println("Monthly interest applied to " + updatedCount + " accounts.");

        } catch (Exception e) {
            System.err.println("Failed to apply monthly interest: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getTotalCustomers() {
        return customerDAO.getTotalCustomersCount();
    }

    public int getTotalEmployees() {
        return employeeDAO.getTotalEmployeesCount();
    }

    public int getTotalAccounts() {
        List<Account> accounts = accountDAO.getAllAccounts();
        return accounts != null ? accounts.size() : 0;
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public List<User> getUsersByType(String userType) {
        return userDAO.getUsersByType(userType);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public List<Transaction> getAllTransactions() {


        return List.of();
    }

    public boolean deleteUser(int userId, String userType) {
        try {
            if ("CUSTOMER".equals(userType)) {
                return customerDAO.deleteCustomer(userId);
            } else if ("EMPLOYEE".equals(userType)) {
                return employeeDAO.deleteEmployee(userId);
            } else {
                return userDAO.deleteUser(userId);
            }
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean createEmployee(Employee employee, String username, String password) {
        try {
            // Creating user account
            User user = new User(username, password, "EMPLOYEE", employee.getFirstName() + "." + employee.getLastName() + "@ebmbank.com");

            if (userDAO.registerUser(user)) {

                employee.setEmployeeId(user.getUserId());
                // for Adding employee details
                return employeeDAO.addEmployee(employee);
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error in createEmployee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public double getTotalBankBalance() {
        List<Account> accounts = accountDAO.getAllAccounts();
        return accounts.stream().mapToDouble(Account::getBalance).sum();
    }

    public int getTotalTransactionsCount() {


        return 1250;
    }
}