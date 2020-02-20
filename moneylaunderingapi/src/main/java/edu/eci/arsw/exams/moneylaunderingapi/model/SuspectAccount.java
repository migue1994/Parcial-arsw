package edu.eci.arsw.exams.moneylaunderingapi.model;

public class SuspectAccount {
    private String accountId;
    private int amountOfSmallTransactions;

    public String getAccountId() {
        return accountId;
    }

    public int getAmountOfSmallTransactions() {
        return amountOfSmallTransactions;
    }

    public void setAmountOfSmallTransactions(int amountOfSmallTransactions) {
        this.amountOfSmallTransactions = amountOfSmallTransactions;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
