package model;

public class ChequeAccount extends Account {

    public ChequeAccount(String accountNumber, double balance, String branch, Customer owner) {
        super(accountNumber, balance, branch, owner);
        this.interestRate = 0.0; // No interest for cheque account
        this.accountType = "Cheque";
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
        return "Cheque";
    }
}