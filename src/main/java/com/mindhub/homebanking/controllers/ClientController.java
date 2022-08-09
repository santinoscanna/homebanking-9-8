package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired // Voy a poder usar el repository para obtener información
    private ClientRepository clientRepository;

    @RequestMapping("/clients")
    public Set<ClientDTO> getclients () {
        return this.clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toSet()); // .stream
    }


    // Preguntar sobre esto que no quedó claro //
    @RequestMapping("/clients/{id}")
    public ClientDTO getClients(@PathVariable Long id) {
        if(clientRepository.findById(id).isPresent())
            return new ClientDTO(clientRepository.findById(id).get());
        return null;
    }
}