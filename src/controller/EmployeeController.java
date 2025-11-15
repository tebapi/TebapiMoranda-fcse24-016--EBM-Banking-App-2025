package controller;

import dao.CustomerDAO;
import dao.AccountDAO;
import dao.TransactionDAO;
import dao.UserDAO;
import model.*;
import java.util.List;

public class EmployeeController {
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private UserDAO userDAO;
    private AuthController authController;

    public EmployeeController() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.userDAO = new UserDAO();
        this.authController = new AuthController();
    }

    // Existing methods
    public List<Customer> getAllCustomers() {
        System.out.println("EmployeeController: Getting all customers");
        List<Customer> customers = customerDAO.getAllCustomers();
        System.out.println("Found " + customers.size() + " customers");
        return customers;
    }

    public Customer getCustomerById(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }

    public List<Customer> searchCustomersByName(String name) {
        return customerDAO.searchCustomersByName(name);
    }

    public boolean openAccount(Account account, int employeeId) {
        return accountDAO.addAccount(account);
    }

    public boolean deleteClient(int customerId) {
        try {
            System.out.println("EmployeeController: Deleting client with ID: " + customerId);
            boolean result = customerDAO.deleteCustomer(customerId);
            System.out.println("Delete result: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("Error deleting client: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public boolean createNewCustomer(String firstName, String lastName, String address,
                                     String email, String phone, String employmentStatus,
                                     String companyName, String companyAddress,
                                     String username, String password) {
        try {
            System.out.println("EmployeeController: Creating new customer - " + firstName + " " + lastName);

            // Create customer object
            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setAddress(address);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setEmploymentStatus(employmentStatus);
            customer.setCompanyName(companyName);
            customer.setCompanyAddress(companyAddress);

            System.out.println("Customer object created, calling authController.registerCustomer...");


            boolean success = authController.registerCustomer(customer, username, password);

            System.out.println("Registration result: " + success);
            return success;

        } catch (Exception e) {
            System.err.println("Error in createNewCustomer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Process deposit
    public boolean processDeposit(String accountNumber, double amount, int employeeId) {
        try {
            System.out.println("Processing deposit - Account: " + accountNumber + ", Amount: " + amount);

            Account account = accountDAO.getAccountByNumber(accountNumber);
            if (account != null && account.canDeposit()) {
                System.out.println("Account found: " + account.getAccountNumber() + ", Balance: " + account.getBalance());

                double newBalance = account.getBalance() + amount;
                System.out.println("New balance will be: " + newBalance);

                if (accountDAO.updateAccountBalance(accountNumber, newBalance)) {
                    System.out.println("Balance updated successfully");

                    // Record transaction
                    Transaction transaction = new Transaction(
                            accountNumber,
                            "DEPOSIT",
                            amount,
                            "Deposit processed by employee ID: " + employeeId
                    );
                    boolean transactionResult = transactionDAO.addTransaction(transaction);
                    System.out.println("Transaction recorded: " + transactionResult);
                    return transactionResult;
                } else {
                    System.out.println("Failed to update balance");
                }
            } else {
                System.out.println("Account not found or cannot deposit");
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error in processDeposit: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Process withdrawal
    public boolean processWithdrawal(String accountNumber, double amount, int employeeId) {
        try {
            System.out.println("Processing withdrawal - Account: " + accountNumber + ", Amount: " + amount);

            Account account = accountDAO.getAccountByNumber(accountNumber);
            if (account != null && account.canWithdraw() && account.getBalance() >= amount) {
                System.out.println("Account found: " + account.getAccountNumber() + ", Balance: " + account.getBalance());

                double newBalance = account.getBalance() - amount;
                System.out.println("New balance will be: " + newBalance);

                if (accountDAO.updateAccountBalance(accountNumber, newBalance)) {
                    System.out.println("Balance updated successfully");

                    // Record transaction
                    Transaction transaction = new Transaction(
                            accountNumber,
                            "WITHDRAWAL",
                            amount,
                            "Withdrawal processed by employee ID: " + employeeId
                    );
                    boolean transactionResult = transactionDAO.addTransaction(transaction);
                    System.out.println("Transaction recorded: " + transactionResult);
                    return transactionResult;
                } else {
                    System.out.println("Failed to update balance");
                }
            } else {
                System.out.println("Account not found, cannot withdraw, or insufficient funds");
                System.out.println("Account: " + account);
                if (account != null) {
                    System.out.println("Can withdraw: " + account.canWithdraw());
                    System.out.println("Balance: " + account.getBalance() + ", Amount: " + amount);
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error in processWithdrawal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Customer> getAllCustomersForSelection() {
        return customerDAO.getAllCustomers();
    }

    public List<Account> getCustomerAccountsForSelection(int customerId) {
        System.out.println("Getting accounts for customer ID: " + customerId);
        List<Account> accounts = accountDAO.getAccountsByCustomerId(customerId);
        System.out.println("Found " + accounts.size() + " accounts");
        return accounts;
    }

    // Get accounts for a specific customer
    public List<Account> getCustomerAccounts(int customerId) {
        return accountDAO.getAccountsByCustomerId(customerId);
    }

    // Update customer information
    public boolean updateCustomer(Customer customer) {


        return true;
    }

    // Open account for existing customer
    public boolean openAccountForCustomer(int customerId, String accountType, double initialDeposit, String branch, int employeeId) {
        try {
            System.out.println("Opening account for customer ID: " + customerId);
            System.out.println("Account Type: " + accountType + ", Deposit: " + initialDeposit + ", Branch: " + branch);

            // Generate account number
            String accountNumber = generateAccountNumber(accountType);
            System.out.println("Generated account number: " + accountNumber);

            // Get customer
            Customer customer = customerDAO.getCustomerById(customerId);
            if (customer == null) {
                System.out.println("Customer not found with ID: " + customerId);
                return false;
            }

            System.out.println("Found customer: " + customer.getFirstName() + " " + customer.getLastName());

            // Create account based on type
            Account account;
            switch (accountType) {
                case "Savings":
                    account = new SavingsAccount(accountNumber, initialDeposit, branch, customer);
                    break;
                case "Investment":
                    if (initialDeposit < 500.00) {
                        System.out.println("Investment account requires minimum BWP 500.00");
                        return false;
                    }
                    account = new InvestmentAccount(accountNumber, initialDeposit, branch, customer);
                    break;
                case "Cheque":
                    account = new ChequeAccount(accountNumber, initialDeposit, branch, customer);
                    break;
                default:
                    System.out.println("Invalid account type: " + accountType);
                    return false;
            }

            System.out.println("Account object created: " + accountNumber);

            // Add account to database
            if (accountDAO.addAccount(account)) {
                System.out.println("Account added to database successfully");

                // Record initial deposit transaction if deposit > 0
                if (initialDeposit > 0) {
                    Transaction transaction = new Transaction(
                            accountNumber,
                            "DEPOSIT",
                            initialDeposit,
                            "Initial deposit - Account opening"
                    );
                    boolean transactionAdded = transactionDAO.addTransaction(transaction);
                    System.out.println("Initial transaction recorded: " + transactionAdded);
                }

                return true;
            } else {
                System.out.println("Failed to add account to database");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error in openAccountForCustomer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private String generateAccountNumber(String accountType) {
        String prefix = "";
        switch (accountType) {
            case "Savings": prefix = "SAV"; break;
            case "Investment": prefix = "INV"; break;
            case "Cheque": prefix = "CHQ"; break;
        }
        return prefix + "-" + System.currentTimeMillis();
    }
}