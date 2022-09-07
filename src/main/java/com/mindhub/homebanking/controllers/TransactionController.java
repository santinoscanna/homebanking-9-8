package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                                    @RequestParam double amount, @RequestParam String description) {
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if (fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || amount == 0 || description.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Account fromAccount = accountRepository.findByNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByNumber(toAccountNumber);

        if (fromAccount == toAccount) {
            return new ResponseEntity<>("Error al ingresar el numero de cuenta de destino", HttpStatus.FORBIDDEN);
        }

        if (fromAccount == null) {
            return new ResponseEntity<>("El numero de cuenta de origen ingresado no existe", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains((fromAccount))) {
            return new ResponseEntity<>("La cuenta de destino no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);
        }

        if (toAccount == null) {
            return new ResponseEntity<>("El numero de cuenta destino ingresado no existe", HttpStatus.FORBIDDEN);
        }

        if (fromAccount.getBalance() < amount) {
            return new ResponseEntity<>("Tu cuenta no tiene el balance necesario para realizar esta transferencia", HttpStatus.FORBIDDEN);
        }

        Transaction fromTransaction = new Transaction(TransactionType.DEBIT, -amount, toAccountNumber + " " + description, fromAccount);
        Transaction toTransaction = new Transaction(TransactionType.CREDIT, +amount, fromAccountNumber + description, toAccount);
        transactionRepository.save(fromTransaction);
        transactionRepository.save(toTransaction);

        fromAccount.setBalance(fromAccount.getBalance()-amount);
        toAccount.setBalance(toAccount.getBalance()+amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return new ResponseEntity<>("201 CREATED", HttpStatus.CREATED);
    }
}

