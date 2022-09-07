package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;


@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
		System.out.println("Bienvenidos");

	}

	@Bean		// Metodo para generar datos de prueba.
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args)->
		{
			LocalDateTime tiempo1 = LocalDateTime.now();
			LocalDateTime tiempo2 = LocalDateTime.now().plusDays(1);

			Client client1 = new Client("Santino", "Scannapieco", "tino@gmail.com", passwordEncoder.encode("112"));
			Account account1 = new Account("VIN001", tiempo1,  5000, AccountType.CORRIENTE);
			Account account2 = new Account("VIN002", tiempo2,  7500, AccountType.AHORRO);

			client1.addAccount(account1);
			client1.addAccount(account2);
			clientRepository.save(client1);
			accountRepository.save(account1);
			accountRepository.save(account2);

			Client client2 = new Client("Carlos", "Tevez", "tevez@gmail.com", passwordEncoder.encode("222"));
			Account account3 = new Account("VIN003", tiempo1,  4000, AccountType.CORRIENTE);
			Account account4 = new Account("VIN004", tiempo2,  3500,  AccountType.CORRIENTE);

			client2.addAccount(account3);
			client2.addAccount(account4);

			clientRepository.save(client2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 2000, "transferencia recibida", LocalDateTime.now(), account1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -4000, "Compra tienda x1", LocalDateTime.now(), account1);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 1000, "transferencia recibida", LocalDateTime.now(), account2);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -200, "Compra tienda x2", LocalDateTime.now(), account2);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);


			Loan loan1 = new Loan("Hipotecario", 500000, Arrays.asList(12,24,36,48,60), 0.25);	// Este prestamo tiene 25% de interes
			Loan loan2 = new Loan("Personal", 100000, Arrays.asList(6,12,24), 0.1);				// Este prestamo tiene 10% de interes
			Loan loan3 = new Loan("Automotriz", 300000, Arrays.asList(6,12,24,36), 0.2);			// Este prestamo tiene 20% de interes

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000, 60, client1, loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12, client1, loan2);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24, client2, loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36, client2, loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			LocalDateTime tiempo3 = LocalDateTime.now().plusYears(5);

			Card card1 = new Card(client1, CardType.DEBIT , CardColor.GOLD, "1556 8795 2345 1589", 123, tiempo1, tiempo3);
			Card card2 = new Card(client1, CardType.CREDIT, CardColor.TITANIUM, "2588 9632 5877 1893", 358, tiempo1, tiempo3);
			Card card3 = new Card(client2 , CardType.CREDIT, CardColor.SILVER, "5968 4271 3365 7583", 574, tiempo1, tiempo3);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);


		};
	}
}

