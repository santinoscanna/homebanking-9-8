package com.mindhub.homebanking.controllers;

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
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.CardUtils.*;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/cards")
    public Set<CardDTO> getcards () {
        return this.cardRepository.findAll().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor){
        Client client = clientRepository.findByEmail(authentication.getName());

        if (cardRepository.findByClientAndType(client, cardType).size()>=3){
            return new ResponseEntity<>("Ya tiene 3 tarjetas de este tipo", HttpStatus.FORBIDDEN);
        }
        Card card = new Card(client, cardType, cardColor, generateCardNumber(1000, 9999, cardRepository), generateCvv(100, 999));
        cardRepository.save(card);
        return new ResponseEntity<>("201 creada", HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @RequestParam String number){
        Client client = clientRepository.findByEmail(authentication.getName());

        // Verifico que el numero de tarjeta sea compatible con alguna de sus tarjetas
        if(!client.getCards().contains(cardRepository.findByNumber(number))) {
            return new ResponseEntity<>("Este numero no es compatible con sus tarjetas", HttpStatus.FORBIDDEN);
        }
        cardRepository.delete(cardRepository.findByNumber(number));
        return new ResponseEntity<>("201 DELETE", HttpStatus.CREATED);
    }

}