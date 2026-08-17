package com.julianmejiac.walletoptimizer.service;

import com.julianmejiac.walletoptimizer.dto.CardRecommendationDTO;
import com.julianmejiac.walletoptimizer.exception.CardNotFoundException;
import com.julianmejiac.walletoptimizer.exception.RewardRuleNotFoundException;
import com.julianmejiac.walletoptimizer.model.Card;
import com.julianmejiac.walletoptimizer.model.RewardRule;
import com.julianmejiac.walletoptimizer.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @InjectMocks
    private CardService cardService;
    private Card card1;
    private Card card2;
    private Card card3;
    @Test
    void getAllCardsTest(){
        //Arrange
        Card costco=new Card("Costco","Citi", "Visa",0.0);
        Card freedom=new Card("Freedom Flex", "Chase","Mastercard",0.0);
        when(cardRepository.findAll()).thenReturn(List.of(costco,freedom));
        //Act
        List<Card> result=cardService.getAllCards();
        //Assert
        assertEquals(List.of(costco,freedom),result);
        verify(cardRepository).findAll();

    }
    @Test
    void getCardsByNameTest(){
        //Arrange
        Card costco=new Card("Costco","Citi", "Visa",0.0);
        Card freedom=new Card("Freedom Flex", "Chase","Mastercard",0.0);
        Card freedom2=new Card("Freedom flex","Chase","Mastercard",5.0);
        when (cardRepository.findAll()).thenReturn(List.of(costco,freedom,freedom2));
        //Act
        List<Card> result=cardService.getCardsByName("freedom flex");
        //Assert
        assertEquals(List.of(freedom,freedom2),result);
        verify(cardRepository).findAll();
    }
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
    private void setupReccommendCards(){
        card1=new Card("Costco","Citi", "Visa",0.0);
        card2=new Card("Freedom Flex", "Chase","Mastercard",0.0);
        card3=new Card("Freedom flex","Chase","Mastercard",5.0);
        RewardRule rewardRule11= new RewardRule("Gas", BigDecimal.valueOf(5.0));
        RewardRule rewardRule12= new RewardRule("Grocery",BigDecimal.valueOf(2.0));
        RewardRule rewardRule21= new RewardRule("Restaurant",BigDecimal.valueOf(3.0));
        RewardRule rewardRule22= new RewardRule("Gas",BigDecimal.valueOf(3.0));
        RewardRule rewardRule31= new RewardRule("Restaurant",BigDecimal.valueOf(3.0));
        RewardRule rewardRule32= new RewardRule("Gas",BigDecimal.valueOf(4.0));
        card1.setRewardRules(List.of(rewardRule11,rewardRule12));
        card2.setRewardRules(List.of(rewardRule21,rewardRule22));
        card3.setRewardRules(List.of(rewardRule31,rewardRule32));
    }
    @Test
    void recommendCardTestHighestCashback(){
        //Arrange
        setupReccommendCards();
        when(cardRepository.findAll()).thenReturn(List.of(card1,card2,card3));
        //Act
        List<CardRecommendationDTO> results=cardService.recommendCard("gas");
        //Assert
        CardRecommendationDTO expected=new CardRecommendationDTO("Costco",BigDecimal.valueOf(5.0));
        assertEquals(List.of(expected),results);

    }
    @Test
    void recommendCardTestTies(){
        //Arrange
        setupReccommendCards();
        when(cardRepository.findAll()).thenReturn(List.of(card1,card2,card3));
        //Act
        List<CardRecommendationDTO> results=cardService.recommendCard("Restaurant");
        //Assert
        CardRecommendationDTO expected1=new CardRecommendationDTO("Freedom Flex",BigDecimal.valueOf(3.0));
        CardRecommendationDTO expected2=new CardRecommendationDTO("Freedom flex",BigDecimal.valueOf(3.0));
        assertEquals(List.of(expected1,expected2),results);

    }
    @Test
    void recommendCardNull(){
        //Arrange
        setupReccommendCards();
        when(cardRepository.findAll()).thenReturn(List.of(card1,card2,card3));
        //Act
        List<CardRecommendationDTO> results=cardService.recommendCard("Transit");
        //Assert
        assertTrue(results.isEmpty() );

    }
}
