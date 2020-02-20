package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {

    private List<SuspectAccount> suspectAccounts = new ArrayList<>();

    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) throws MoneyException {
        Optional<SuspectAccount> susA = suspectAccounts.stream().filter(sa->sa.getAccountId().equals(suspectAccount.getAccountId())).findFirst();
        susA.get().setAmountOfSmallTransactions(suspectAccount.getAmountOfSmallTransactions());
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) throws MoneyException {
         Optional<SuspectAccount> sa =  suspectAccounts.stream().filter(s -> s.getAccountId() == accountId).findFirst();
         if(sa.get() == null){
             throw new MoneyException("no existe");
         }
        return sa.get();
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        return suspectAccounts;
    }

    @Override
    public void addAccount(SuspectAccount suspectAccount) throws MoneyException {
        for(SuspectAccount sa : suspectAccounts){
            if(sa.equals(suspectAccount)){
                throw new MoneyException("El registro ya existe");
            }
        }
        suspectAccounts.add(suspectAccount);
    }
}
