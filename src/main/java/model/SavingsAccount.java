package model;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountNumber, double balance, String branch, Customer owner) {
        super(accountNumber, balance, branch, owner);
        this.interestRate = 0.0005; // 0.05% monthly
        this.accountType = "Savings";
    }

    @Override
    public boolean canWithdraw() {
        return false;
    }

    @Override
    public boolean canDeposit() {
        return true;
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }
}