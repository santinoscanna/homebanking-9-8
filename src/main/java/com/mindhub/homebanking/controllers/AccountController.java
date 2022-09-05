package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;

import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.AccountUtils.generateAccountNumber;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired // Voy a poder usar el repository para obtener información
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/accounts")
    public Set<AccountDTO> getaccounts () {
        return this.accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }

    // Preguntar sobre esto que no quedó claro //
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable Long id) {
        if(accountRepository.findById(id).isPresent())
            return new AccountDTO(accountRepository.findById(id).get());
        return null;
    }

    // Obtengo las cuentas del cliente autenticado
    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentClientAccounts(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(client.getAccounts().size() >= 3){
            return new ResponseEntity<>("Ya tiene 3 cuentas", HttpStatus.FORBIDDEN);
        }
        Account account = new Account(generateAccountNumber(10000000, 99999999, accountRepository), LocalDateTime.now(), 0);
        client.addAccount(account);
        accountRepository.save(account);
        return new ResponseEntity<>("201 creada", HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam String number){
        Client client = this.clientRepository.findByEmail(authentication.getName());

        // Verifico que el numero de cuenta sea compatible con alguna de sus cuentas
        if(!client.getAccounts().contains(accountRepository.findByNumber(number))) {
            return new ResponseEntity<>("Este numero no es compatible con sus cuentas", HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findByNumber(number).getBalance() > 0) {
            return new ResponseEntity<>("No puedes eliminar esta cuenta ya que todavia tiene balance", HttpStatus.FORBIDDEN);
        }

        Set<Transaction> deleteTransaction = accountRepository.findByNumber(number).getTransactions();
        transactionRepository.deleteAll(deleteTransaction);
        accountRepository.delete(accountRepository.findByNumber(number));
        return new ResponseEntity<>("201 DELETE", HttpStatus.CREATED);
    }

}
