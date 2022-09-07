package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;

import com.mindhub.homebanking.repositories.PaymentRepository;
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
public class PaymentController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/payments")
    public Set<PaymentDTO> getPayment () {
        return this.paymentRepository.findAll().stream().map(payment -> new PaymentDTO(payment)).collect(Collectors.toSet());
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

        return new ResponseEntity<>("201 CREATED", HttpStatus.CREATED);
    }
}
