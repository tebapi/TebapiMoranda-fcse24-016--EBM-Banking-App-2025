package model;

public class SavingsAccount extends Account implements InterestBearing {

    public SavingsAccount(String accountNumber, double balance, String branch, Customer owner) {
        super(accountNumber, balance, branch, owner);
    }

    @Override
    public void calculateInterest() {
        double interest = getBalance() * 0.0005; // 0.05% monthly
        deposit(interest);
    }
}
