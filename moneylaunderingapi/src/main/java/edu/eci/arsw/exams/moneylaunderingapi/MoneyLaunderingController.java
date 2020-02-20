package edu.eci.arsw.exams.moneylaunderingapi;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyException;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fraud-bank-accounts")
public class MoneyLaunderingController
{
    @Autowired
    MoneyLaunderingService moneyLaunderingService;

    @GetMapping("")
    public List<SuspectAccount> offendingAccounts() {
        return moneyLaunderingService.getSuspectAccounts();
    }

    @GetMapping("/fraud-bank-account/{accountId}")
    public ResponseEntity<?> getAcount(@PathVariable String accountId){
        try{
            return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(accountId), HttpStatus.ACCEPTED);
            
        }catch(MoneyException me){
            return new ResponseEntity<>("El registro no existe", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("fraud-bank-account/{accountId}")
    public ResponseEntity<?> putBlueprints(@PathVariable String accountId, @RequestBody SuspectAccount sa) throws MoneyException{
        try{
            moneyLaunderingService.updateAccountStatus(sa);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (MoneyException e){
            return new ResponseEntity<>("No se pudo actualizar",HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addSuspectAccount(@RequestBody SuspectAccount sa){
        try{
            moneyLaunderingService.addAccount(sa);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch(MoneyException me){
            return new ResponseEntity<>("No fue posible añadir l registro", HttpStatus.FORBIDDEN);
        }
    }

}
