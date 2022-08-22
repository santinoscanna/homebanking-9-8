package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;


    @RequestMapping("/cards")
    public Set<CardDTO> getcards () {
        return this.cardRepository.findAll().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor){
        Client client =clientRepository.findByEmail(authentication.getName());

        if (cardRepository.findByClientAndType(client, cardType).size()>=3){
            return new ResponseEntity<>("Ya tiene 3 tarjetas de este tipo", HttpStatus.FORBIDDEN);
        }
        Card card = new Card(client, cardType, cardColor);
        client.addCard(card);
        cardRepository.save(card);
        return new ResponseEntity<>("201 creada", HttpStatus.CREATED);
    }

}
