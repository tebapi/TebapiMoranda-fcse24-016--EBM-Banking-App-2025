package controller;

import dao.*;
import model.*;
import java.util.List;

public class BankingController {
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    public BankingController() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    // Customer operations
    public boolean registerCustomer(Customer customer) {
        return customerDAO.addCustomer(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public Customer getCustomer(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }

    // Account operations
    public boolean openAccount(Account account) {
        if (accountDAO.addAccount(account)) {
            // Record initial deposit transaction
            Transaction transaction = new Transaction(
                    account.getAccountNumber(),
                    "DEPOSIT",
                    account.getBalance(),
                    "Initial account opening deposit"
            );
            transactionDAO.addTransaction(transaction);
            return true;
        }
        return false;
    }

    public List<Account> getCustomerAccounts(int customerId) {
        return accountDAO.getAccountsByCustomerId(customerId);
    }

    // Transaction operations
    public boolean deposit(String accountNumber, double amount) {
        List<Account> accounts = getCustomerAccounts(getCustomerIdFromAccount(accountNumber));
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                if (account.canDeposit()) {
                    account.deposit(amount);
                    if (accountDAO.updateAccountBalance(accountNumber, account.getBalance())) {
                        Transaction transaction = new Transaction(
                                accountNumber,
                                "DEPOSIT",
                                amount,
                                "Cash deposit"
                        );
                        return transactionDAO.addTransaction(transaction);
                    }
                }
                break;
            }
        }
        return false;
    }

    public boolean withdraw(String accountNumber, double amount) {
        List<Account> accounts = getCustomerAccounts(getCustomerIdFromAccount(accountNumber));
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                if (account.withdraw(amount)) {
                    if (accountDAO.updateAccountBalance(accountNumber, account.getBalance())) {
                        Transaction transaction = new Transaction(
                                accountNumber,
                                "WITHDRAWAL",
                                amount,
                                "Cash withdrawal"
                        );
                        return transactionDAO.addTransaction(transaction);
                    }
                }
                break;
            }
        }
        return false;
    }

    public void applyMonthlyInterest() {
        List<Customer> customers = getAllCustomers();
        for (Customer customer : customers) {
            List<Account> accounts = getCustomerAccounts(customer.getCustomerId());
            for (Account account : accounts) {
                if (account.getInterestRate() > 0) {
                    double oldBalance = account.getBalance();
                    account.applyInterest();
                    double interestAmount = account.getBalance() - oldBalance;

                    if (accountDAO.updateAccountBalance(account.getAccountNumber(), account.getBalance())) {
                        Transaction transaction = new Transaction(
                                account.getAccountNumber(),
                                "INTEREST",
                                interestAmount,
                                "Monthly interest payment"
                        );
                        transactionDAO.addTransaction(transaction);
                    }
                }
            }
        }
    }

    public List<Transaction> getAccountTransactions(String accountNumber) {
        return transactionDAO.getTransactionsByAccount(accountNumber);
    }

    private int getCustomerIdFromAccount(String accountNumber) {


        return 1;
    }
}