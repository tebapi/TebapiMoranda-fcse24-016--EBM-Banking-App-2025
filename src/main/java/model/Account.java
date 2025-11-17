package model;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer owner;
    protected double interestRate;
    protected String accountType;

    public Account(String accountNumber, double balance, String branch, Customer owner) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.owner = owner;
    }

    // Abstract methods
    public abstract boolean canWithdraw();
    public abstract boolean canDeposit();
    public abstract String getAccountType();

    // Concrete methods
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance && canWithdraw()) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void applyInterest() {
        if (interestRate > 0) {
            double interest = balance * interestRate;
            balance += interest;
        }
    }

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public Customer getOwner() { return owner; }
    public void setOwner(Customer owner) { this.owner = owner; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    @Override
    public String toString() {
        return accountType + " Account: " + accountNumber + " - Balance: BWP " + balance;
    }
}