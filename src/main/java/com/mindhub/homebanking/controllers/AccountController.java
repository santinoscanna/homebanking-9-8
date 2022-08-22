package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired // Voy a poder usar el repository para obtener información
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public Set<AccountDTO> getaccounts () {
        return this.accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }

    // Preguntar sobre esto que no quedó claro //
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable Long id) {
        if(accountRepository.findById(id).isPresent())
            return new AccountDTO(accountRepository.findById(id).get());
        return null;
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(client.getAccounts().size() >= 3){
            return new ResponseEntity<>("Ya tiene 3 cuentas", HttpStatus.FORBIDDEN);
        }
        Account account = new Account(generateAccountNumber(), LocalDateTime.now(), 0);
        client.addAccount(account);
        accountRepository.save(account);
        return new ResponseEntity<>("201 creada", HttpStatus.CREATED);
    }

    public String generateAccountNumber(){
        return "VIN"+((int)((Math.random()*(99999999-10000000))+10000000));
    }
}
