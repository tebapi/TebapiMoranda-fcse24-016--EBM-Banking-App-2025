package model;

public class Account {
    private String accountNumber;
    private double balance;
    private String branch;
    private Customer owner;

    public Account(String accountNumber, double balance, String branch, Customer owner) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.owner = owner;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber; }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber; }

    public double getBalance() {
        return balance; }
    public void setBalance(double balance) {
        this.balance = balance; }

    public String getBranch() {
        return branch; }
    public void setBranch(String branch) {
        this.branch = branch; }

    public Customer getOwner() {
        return owner; }
    public void setOwner(Customer owner) {
        this.owner = owner; }
}

