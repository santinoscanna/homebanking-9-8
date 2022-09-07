package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //base de datos real, no enbebida como h2
public class RepositoriesTest {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;
    // -------Test Client------- //
    @Test
    public void existsClient() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }
    @Test
    public void existsClientEmail() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("email", is("tino@gmail.com"))));

    }
    // -------Test Account------- //
    @Test
    public void existsAccount() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
    }
    @Test
    public void existsAccountNumber() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number", is("VIN001"))));
    }
    // -------Test Loan------- //
    @Test
    public void existsLoans() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));

    }
    @Test
    public void existsLoanPersonal() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }
    // -------Test Card------- //
    @Test
    public void existsCard() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }
    @Test
    public void existsCardDebit() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("type", is(CardType.DEBIT))));
    }
    // -------Test Transaction //
    @Test
    public void existsTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }
    @Test
    public void existsTransactionDebit() {
        List<Transaction> transactions = transactionRepository.findAll();
    assertThat(transactions, hasItem(hasProperty("type", is(TransactionType.DEBIT))));
    }
}
