package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Client;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    // Lo utilizo para solo agarra el atributo de Client
    // que quiero guardar en ClientDTO y mostrarlas
    // Esta clase la puedo usar para NO mostrar todos los datos (solo los que yo elijo)
    private long id;
    private String firstName, lastName, email; // Atributo que quiero mostrar

    public Set<AccountDTO> accounts;
    public Set<ClientLoanDTO> loans;
    public Set<CardDTO> cards;
    public ClientDTO() { // Constructor limpio
    }

    public ClientDTO(Client client) { // Constructor que recibe todos los datos del cliente y guarda solo el firstName
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
        this.loans = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}
