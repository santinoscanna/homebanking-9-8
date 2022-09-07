package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType type;

    public Set<TransactionDTO> transactions;
    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.type = account.getType();
        this.creationDate = account.getCreationDate();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
    }

    Set<TransactionDTO> transaction = new HashSet<>();

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public AccountType getType() {
        return type;
    }
}
