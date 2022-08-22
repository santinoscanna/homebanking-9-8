package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    private String cardholder;
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;


    public Card() {
    }

    public Card(Client client, CardType type, CardColor color, String number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate) {
        this.client = client;
        this.cardholder = client.getFullName();
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }

    public Card(Client client, CardType type, CardColor color) {
        this.client = client;
        this.type = type;
        this.color = color;
        this.number = getFullNumber();
        this.cvv = ((int)((Math.random()*(999-100))+100));
        this.fromDate = LocalDateTime.now();
        this.thruDate = LocalDateTime.now().plusYears(5);
    }

    public String getFullNumber() {     //Genero 4 numeros y los concateno para crear el numero de la Card
        int num1 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        int num2 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        int num3 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        int num4 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        return num1 + "-" + num2 + "-" + num3 + "-" + num4;
    }

    public long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }
}
