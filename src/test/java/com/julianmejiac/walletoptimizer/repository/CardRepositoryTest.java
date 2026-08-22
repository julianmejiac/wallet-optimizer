package com.julianmejiac.walletoptimizer.repository;

import com.julianmejiac.walletoptimizer.model.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CardRepositoryTest {
    @Autowired
    private CardRepository cardRepository;
    //This Test checks that correct info is saved in a Card
    @Test
    void SaveAndFindCardTest(){
        //Arrange
        Card card=new Card("Costco","Citi","Visa",0.0, BigDecimal.valueOf(1.5));
        // Act
        Card savedCard = cardRepository.save(card);

        Optional<Card> result =
                cardRepository.findById(savedCard.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Costco", result.get().getName());
        assertEquals("Citi", result.get().getIssuer());
        assertEquals("Visa", result.get().getNetwork());
        assertEquals(0.0, result.get().getAnnualFee());
        assertEquals(BigDecimal.valueOf(1.5),result.get().getDefaultCashbackPercent());
        assertTrue(result.get().isActive());
    }

}
