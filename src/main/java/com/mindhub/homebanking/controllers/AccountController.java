package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired // Voy a poder usar el repository para obtener información
    private AccountRepository accountRepository;

    @RequestMapping("/accounts")
    public Set<AccountDTO> getcuenta () {
        return this.accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet()); // .stream
    }

    // Preguntar sobre esto que no quedó claro //
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable Long id) {
        if(accountRepository.findById(id).isPresent())
            return new AccountDTO(accountRepository.findById(id).get());
        return null;
    }
}
