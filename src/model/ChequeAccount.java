package model;

public class ChequeAccount extends Account {
    private String companyName;
    private String companyAddress;

    public ChequeAccount(String accountNumber, double balance, String branch, Customer owner,
                         String companyName, String companyAddress) {
        super(accountNumber, balance, branch, owner);
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= getBalance()) {
            setBalance(getBalance() - amount);
        }
    }

    // Getters and Setters
    public String getCompanyName() {
        return companyName; }
    public void setCompanyName(String companyName) {
        this.companyName = companyName; }

    public String getCompanyAddress() {
        return companyAddress; }
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress; }
}
