package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;

public interface MoneyLaunderingService {
    void updateAccountStatus(SuspectAccount suspectAccount) throws MoneyException  ;
    SuspectAccount getAccountStatus(String accountId) throws MoneyException  ;
    List<SuspectAccount> getSuspectAccounts();
    void addAccount(SuspectAccount suspectAccount) throws MoneyException;
}
