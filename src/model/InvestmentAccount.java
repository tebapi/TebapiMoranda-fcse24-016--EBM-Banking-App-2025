package model;

public class InvestmentAccount extends Account {
    private static final double MIN_OPENING_BALANCE = 500.00;

    public InvestmentAccount(String accountNumber, double balance, String branch, Customer owner) {
        super(accountNumber, balance, branch, owner);
        if (balance < MIN_OPENING_BALANCE) {
            throw new IllegalArgumentException("Investment account requires minimum BWP 500.00 opening balance");
        }
        this.interestRate = 0.05; // monthly
        this.accountType = "Investment";
    }

    @Override
    public boolean canWithdraw() {
        return true;
    }

    @Override
    public boolean canDeposit() {
        return true;
    }

    @Override
    public String getAccountType() {
        return "Investment";
    }

    public static double getMinOpeningBalance() {
        return MIN_OPENING_BALANCE;
    }
}