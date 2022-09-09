package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/payments")
    public List<PaymentDTO> getPayment () {
        return this.paymentRepository.findAll().stream().map(PaymentDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping(path = "/clients/current/payments")
    public ResponseEntity<Object> realizePayment(Authentication authentication, @RequestBody PaymentDTO paymentDTO){

        String accountNumber = paymentDTO.getAccountNumber();
        String cardNumber = paymentDTO.getCardNumber();
        String description = paymentDTO.getDescription();
        int cvv = paymentDTO.getCvv();
        double amount = paymentDTO.getAmount();

        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(accountNumber);
        Card card = cardRepository.findByNumber(cardNumber);

        double accountBalance = account.getBalance();

        if(accountNumber.isEmpty() || cardNumber.isEmpty() || description.isEmpty() || amount < 1) {
            return new ResponseEntity<>("Mising data", HttpStatus.FORBIDDEN);
        }

        if(card == null) {
            return new ResponseEntity<>("Esta tarjeta no exiate", HttpStatus.FORBIDDEN);
        }

        if(account == null) {
            return new ResponseEntity<>("Esta cuanta no existe", HttpStatus.FORBIDDEN);
        }

        if(!client.getCards().contains(card)) {
            return new ResponseEntity<>("Esta tarjeta no esta vinculada con su cuenta", HttpStatus.FORBIDDEN);
        }

        Payment fromAccount = new Payment(accountNumber, cardNumber, cvv, -amount, description);
        paymentRepository.save(fromAccount);
        account.setBalance(accountBalance-amount);
        accountRepository.save(account);
        Transaction transaction = new Transaction(TransactionType.DEBIT, -amount, description, account);
        accountRepository.save(account);
        transactionRepository.save(transaction);

        return new ResponseEntity<>("201 CREATED", HttpStatus.CREATED);
    }
}
