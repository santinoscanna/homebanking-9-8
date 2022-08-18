package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

import java.util.Set;


@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired // Voy a poder usar el repository para obtener información
    private ClientRepository clientRepository;

    @RequestMapping("/clients")
    public Set<ClientDTO> getclients () {
        return this.clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toSet()); // .stream
    }


    // Preguntar sobre esto que no quedó claro //
    @RequestMapping("/clients/{id}")
    public ClientDTO getClients(@PathVariable Long id) {
        if(clientRepository.findById(id).isPresent())
            return new ClientDTO(clientRepository.findById(id).get());
        return null;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> createClient(@RequestParam String firstName,  @RequestParam String lastName,
                                               @RequestParam String email,      @RequestParam String password){
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}