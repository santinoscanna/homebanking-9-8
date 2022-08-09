package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
		System.out.println("Bienvenidos");

	}

	@Bean		// Metodo para generar datos de prueba.
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args)->
		{
			LocalDateTime tiempo1 = LocalDateTime.now();
			LocalDateTime tiempo2 = LocalDateTime.now().plusDays(1);

			Client client1 = new Client("Santino", "Scannapieco", "tino@gmail.com");
			Account account1 = new Account("VIN001", tiempo1,  5000);
			Account account2 = new Account("VIN002", tiempo2,  7500);

			client1.addAccount(account1);
			client1.addAccount(account2);

			clientRepository.save(client1);
			accountRepository.save(account1);
			accountRepository.save(account2);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 2000, "transferencia recibida", LocalDateTime.now(), account1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -4000, "Compra tienda x1", LocalDateTime.now(), account1);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 1000, "transferencia recibida", LocalDateTime.now(), account2);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -200, "Compra tienda x2", LocalDateTime.now(), account2);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);

		};
	}
}
