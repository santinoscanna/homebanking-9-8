package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource     //le heredo los metodos que ya estan armados de JpaRepository a ClientRepository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
