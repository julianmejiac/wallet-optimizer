package com.julianmejiac.walletoptimizer.service;

import com.julianmejiac.walletoptimizer.exception.CardNotFoundException;
import com.julianmejiac.walletoptimizer.model.Card;
import com.julianmejiac.walletoptimizer.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @InjectMocks
    private CardService cardService;

    @Test
    void getCardByIdReturnsExistingCard(){
        //Arrange
        Card costco= new Card("Costco","Citi", "Visa",0.0);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(costco));
        //Act
        Card result=cardService.getCardById(1L);
        //Assert
        assertSame(costco,result);
    }
    @Test
    void getCardByIdThrowsCardNotFoundException(){
        //Arrange
        when(cardRepository.findById(99L)).thenReturn(Optional.empty());

        //Assert
        assertThrows(CardNotFoundException.class,()-> cardService.getCardById(99L));
    }

}
