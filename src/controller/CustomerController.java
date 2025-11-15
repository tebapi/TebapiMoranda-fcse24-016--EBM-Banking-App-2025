package controller;

import dao.AccountDAO;
import dao.TransactionDAO;
import dao.CustomerDAO;
import model.*;
import java.util.List;

public class CustomerController {
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private CustomerDAO customerDAO;

    public CustomerController() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.customerDAO = new CustomerDAO();
    }


    // Get customer accounts
    public List<Account> getCustomerAccounts(int customerId) {
        return accountDAO.getAccountsByCustomerId(customerId);
    }

    // Get account transactions
    public List<Transaction> getAccountTransactions(String accountNumber) {
        return transactionDAO.getTransactionsByAccount(accountNumber);
    }

    // Get total balance
    public double getTotalBalance(int customerId) {
        List<Account> accounts = getCustomerAccounts(customerId);
        return accounts.stream().mapToDouble(Account::getBalance).sum();
    }

    // Deposit money
    public boolean deposit(String accountNumber, double amount) {
        try {
            Account account = accountDAO.getAccountByNumber(accountNumber);
            if (account != null && account.canDeposit()) {
                double newBalance = account.getBalance() + amount;
                if (accountDAO.updateAccountBalance(accountNumber, newBalance)) {
                    Transaction transaction = new Transaction(
                            accountNumber,
                            "DEPOSIT",
                            amount,
                            "Customer deposit"
                    );
                    return transactionDAO.addTransaction(transaction);
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Withdraw money
    public boolean withdraw(String accountNumber, double amount) {
        try {
            Account account = accountDAO.getAccountByNumber(accountNumber);
            if (account != null && account.canWithdraw() && account.getBalance() >= amount) {
                double newBalance = account.getBalance() - amount;
                if (accountDAO.updateAccountBalance(accountNumber, newBalance)) {
                    Transaction transaction = new Transaction(
                            accountNumber,
                            "WITHDRAWAL",
                            amount,
                            "Customer withdrawal"
                    );
                    return transactionDAO.addTransaction(transaction);
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Transfer money (between own accounts)
    public boolean transfer(String fromAccount, String toAccount, double amount, int customerId) {
        try {
            // Verify both accounts belong to the same customer
            Account sourceAccount = accountDAO.getAccountByNumber(fromAccount);
            Account targetAccount = accountDAO.getAccountByNumber(toAccount);

            if (sourceAccount == null || targetAccount == null) {
                return false;
            }

            if (sourceAccount.getOwner().getCustomerId() != customerId ||
                    targetAccount.getOwner().getCustomerId() != customerId) {
                return false;
            }

            if (!sourceAccount.canWithdraw() || sourceAccount.getBalance() < amount) {
                return false;
            }

            // Perform transfer
            double sourceNewBalance = sourceAccount.getBalance() - amount;
            double targetNewBalance = targetAccount.getBalance() + amount;

            if (accountDAO.updateAccountBalance(fromAccount, sourceNewBalance) &&
                    accountDAO.updateAccountBalance(toAccount, targetNewBalance)) {

                // Record withdrawal transaction
                Transaction withdrawal = new Transaction(
                        fromAccount,
                        "TRANSFER_OUT",
                        amount,
                        "Transfer to " + toAccount
                );

                // Record deposit transaction
                Transaction deposit = new Transaction(
                        toAccount,
                        "TRANSFER_IN",
                        amount,
                        "Transfer from " + fromAccount
                );

                transactionDAO.addTransaction(withdrawal);
                transactionDAO.addTransaction(deposit);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get customer details
    public Customer getCustomerDetails(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }
}