package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @RequestMapping(path = "/loans", method = RequestMethod.GET)
    public List<LoanDTO> getAllLoans(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> applyingForLoans(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        Client client = clientRepository.findByEmail(authentication.getName());
        double amount = loanApplicationDTO.getAmount();
        int payments = loanApplicationDTO.getPayments();
        String toAccountNumber = loanApplicationDTO.getToAccountNumber();
        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId());
        Account account = accountRepository.findByNumber(toAccountNumber);

        if (amount < 1 || payments < 0 || loanApplicationDTO == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO == null) {
            return new ResponseEntity<>("El prestamo no existe", HttpStatus.FORBIDDEN);
        }

        if (loan.getMaxAmount() < amount) {
            return new ResponseEntity<>("El monto solicitado excede el monto maximo del prestamo", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(payments)) {
            return new ResponseEntity<>("La cantidad de cuotas no esta disponible", HttpStatus.FORBIDDEN);
        }

        if (account == null) {
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains((account))) {
            return new ResponseEntity<>("La cuenta no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);
        }

        String description = "Loan Approved";
        Transaction transaction = new Transaction(TransactionType.CREDIT, amount, description, account);

        ClientLoan clientLoan = new ClientLoan(amount + (amount * 0.2), payments, client, loan);
        clientLoanRepository.save(clientLoan);

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        return new ResponseEntity<>("201 CREATED", HttpStatus.CREATED);
    }
}
