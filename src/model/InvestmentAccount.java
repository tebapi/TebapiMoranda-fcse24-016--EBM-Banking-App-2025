package model;

public class InvestmentAccount extends Account implements InterestBearing {

    public InvestmentAccount(String accountNumber, double balance, String branch, Customer owner) {
        super(accountNumber, balance, branch, owner);
    }

    @Override
    public void calculateInterest() {
        double interest = getBalance() * 0.05; // 5% monthly
        deposit(interest);
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= getBalance()) {
            setBalance(getBalance() - amount);
        }
    }
}
