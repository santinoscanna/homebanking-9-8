package com.mindhub.homebanking;

import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardUtilsTest {

    @Autowired
    CardRepository cardRepository;

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.generateCardNumber(1000, 9999, cardRepository);
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }
    @Test
    public void cardCVVIsCreated(){
        int  cardCvv = CardUtils.generateCvv(100, 999);
        assertThat(cardCvv,greaterThanOrEqualTo(0) );
    }
}