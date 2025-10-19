package controller;

import dao.AccountDao;
import dao.CustomerDao;
import model.*;
import view.BankView;

import java.sql.SQLException;
import java.util.Scanner;

public class BankController {
    private CustomerDao customerDao;
    private AccountDao accountDao;
    private BankView view;
    private Scanner sc;

    public BankController() {
        customerDao = new CustomerDao();
        accountDao = new AccountDao();
        view = new BankView();
        sc = new Scanner(System.in);
    }

    public void run() throws SQLException {
        while (true) {
            view.showMenu();
            int choice = sc.nextInt();
            sc.nextLine(); // clear newline

            switch (choice) {
                case 1 -> addCustomer();
                case 2 -> addAccount();
                case 3 -> view.displayCustomers(customerDao.getAllCustomers());
                case 4 -> view.displayAccounts(accountDao.getAllAccounts());
                case 5 -> updateCustomer();
                case 6 -> deleteCustomer();
                case 7 -> updateAccountBalance();
                case 8 -> deleteAccount();
                case 9 -> {
                    System.out.println("Exiting system...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void addCustomer() throws SQLException {
        System.out.print("Enter Customer ID: ");
        String id = sc.nextLine();
        System.out.print("Enter First Name: ");
        String first = sc.nextLine();
        System.out.print("Enter Surname: ");
        String last = sc.nextLine();
        System.out.print("Enter Address: ");
        String addr = sc.nextLine();

        Customer c = new Customer(id, first, last, addr);
        customerDao.saveCustomer(c);
        view.showMessage("Customer added successfully!");
    }

    private void addAccount() throws SQLException {
        System.out.print("Enter Account Number: ");
        String accNum = sc.nextLine();
        System.out.print("Enter Customer ID: ");
        String custId = sc.nextLine();
        System.out.print("Enter Balance: ");
        double balance = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Branch: ");
        String branch = sc.nextLine();

        Customer owner = customerDao.getCustomerById(custId);
        if (owner == null) {
            view.showMessage("Customer not found!");
            return;
        }

        Account acc = new Account(accNum, balance, branch, owner);
        accountDao.saveAccount(acc);
        view.showMessage("Account added successfully!");
    }

    private void updateCustomer() throws SQLException {
        System.out.print("Enter Customer ID to update: ");
        String id = sc.nextLine();
        Customer c = customerDao.getCustomerById(id);
        if (c == null) {
            view.showMessage("Customer not found!");
            return;
        }

        System.out.print("Enter new First Name: ");
        c.setFirstName(sc.nextLine());
        System.out.print("Enter new Surname: ");
        c.setSurName(sc.nextLine());
        System.out.print("Enter new Address: ");
        c.setAddress(sc.nextLine());

        boolean success = customerDao.updateCustomer(c);
        view.showMessage(success ? "Customer updated successfully!" : "Update failed!");
    }

    private void deleteCustomer() throws SQLException {
        System.out.print("Enter Customer ID to delete: ");
        String id = sc.nextLine();
        boolean success = customerDao.deleteCustomer(id);
        view.showMessage(success ? "Customer deleted!" : "Delete failed!");
    }

    private void updateAccountBalance() throws SQLException {
        System.out.print("Enter Account Number: ");
        String accNum = sc.nextLine();
        System.out.print("Enter new Balance: ");
        double newBal = sc.nextDouble();
        boolean success = accountDao.updateAccountBalance(accNum, newBal);
        view.showMessage(success ? "Account updated!" : "Update failed!");
    }

    private void deleteAccount() throws SQLException {
        System.out.print("Enter Account Number to delete: ");
        String accNum = sc.nextLine();
        boolean success = accountDao.deleteAccount(accNum);
        view.showMessage(success ? "Account deleted!" : "Delete failed!");
    }
}
