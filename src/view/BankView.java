package view;

import model.Customer;
import model.Account;
import java.util.List;

public class BankView {

    public void showMenu() {
        System.out.println("\n====== BANKING SYSTEM MENU ======");
        System.out.println("1. Add Customer");
        System.out.println("2. Add Account");
        System.out.println("3. View All Customers");
        System.out.println("4. View All Accounts");
        System.out.println("5. Update Customer");
        System.out.println("6. Delete Customer");
        System.out.println("7. Update Account Balance");
        System.out.println("8. Delete Account");
        System.out.println("9. Exit");
        System.out.print("Enter choice: ");
    }

    // ✅ Display list of customers
    public void displayCustomers(List<Customer> customers) {
        System.out.println("\n--- CUSTOMER LIST ---");
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer c : customers) {
                System.out.println("ID: " + c.getCustomerId() +
                        " | Name: " + c.getFirstName() + " " + c.getSurName() +
                        " | Address: " + c.getAddress());
            }
        }
    }

    // ✅ Display list of accounts
    public void displayAccounts(List<Account> accounts) {
        System.out.println("\n--- ACCOUNT LIST ---");
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (Account a : accounts) {
                System.out.println("Acc#: " + a.getAccountNumber() +
                        " | Branch: " + a.getBranch() +
                        " | Balance: BWP " + a.getBalance() +
                        " | Owner ID: " + a.getOwner().getCustomerId());
            }
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
