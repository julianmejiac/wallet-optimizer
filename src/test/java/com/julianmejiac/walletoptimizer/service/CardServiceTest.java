package com.julianmejiac.walletoptimizer.service;

import com.julianmejiac.walletoptimizer.dto.CardRecommendationDTO;
import com.julianmejiac.walletoptimizer.dto.CreateCardRequest;
import com.julianmejiac.walletoptimizer.dto.RewardRuleRequest;
import com.julianmejiac.walletoptimizer.dto.UpdateCardRequest;
import com.julianmejiac.walletoptimizer.exception.CardNotFoundException;
import com.julianmejiac.walletoptimizer.exception.DuplicateCardException;
import com.julianmejiac.walletoptimizer.exception.RewardRuleNotFoundException;
import com.julianmejiac.walletoptimizer.model.Card;
import com.julianmejiac.walletoptimizer.model.RewardRule;
import com.julianmejiac.walletoptimizer.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        Card costco=new Card("Costco","Citi", "Visa",BigDecimal.valueOf(0.0),BigDecimal.valueOf(1.5));
        Card freedom=new Card("Freedom Flex", "Chase","Mastercard",BigDecimal.valueOf(0.0),BigDecimal.valueOf(1.5));
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
        Card costco=new Card("Costco","Citi", "Visa",BigDecimal.valueOf(0.0),BigDecimal.valueOf(1.5));
        Card freedom=new Card("Freedom Flex", "Chase","Mastercard",BigDecimal.valueOf(0.0),BigDecimal.valueOf(1.5));
        Card freedom2=new Card("Freedom flex","Chase","Mastercard",BigDecimal.valueOf(5.0),BigDecimal.valueOf(1.5));
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
        Card costco= new Card("Costco","Citi", "Visa",BigDecimal.valueOf(0.0),BigDecimal.valueOf(1.5));
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
        card1=new Card("Costco","Citi", "Visa",BigDecimal.valueOf(0.0),BigDecimal.valueOf(1.5));
        card2=new Card("Freedom Flex", "Chase","Mastercard",BigDecimal.valueOf(0.0),BigDecimal.valueOf(1.5));
        card3=new Card("Freedom flex","Huntington","Mastercard",BigDecimal.valueOf(5.0),BigDecimal.valueOf(1.0));
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
    void shouldReturnCardWithBestDefaultCashbackWhenCategoryIsUnknown(){
        //Arrange
        setupReccommendCards();
        when(cardRepository.findAll()).thenReturn(List.of(card1,card2,card3));
        //Act
        List<CardRecommendationDTO> results=cardService.recommendCard("Transit");
        //Assert (This time result is not empty since we have default cashback)
        CardRecommendationDTO expected1=new CardRecommendationDTO("Costco",BigDecimal.valueOf(1.5));
        CardRecommendationDTO expected2=new CardRecommendationDTO("Freedom Flex",BigDecimal.valueOf(1.5));
        assertEquals(List.of(expected1,expected2),results);

    }
    @Test
    void addCardThrowsDuplicateCardException() {
        // Arrange
        CreateCardRequest request = new CreateCardRequest(
                "Costco",
                "Citi",
                "Visa",
                BigDecimal.valueOf(0.0),
                BigDecimal.valueOf(1.5)
        );

        //  Tell Mockito that a matching card already exists.
        when(cardRepository.existsByNameIgnoreCaseAndIssuerIgnoreCase(request.name(), request.issuer())).thenReturn(true);

        //  Assert that cardService.addCard(request)throws DuplicateCardException.
        assertThrows(DuplicateCardException.class,()->cardService.addCard(request));


        //  Verify that cardRepository.save() was never called.
        verify(cardRepository,never()).save(any(Card.class));
    }
    @Test
    void addCardSuccessfully() {
        // Arrange
        CreateCardRequest request = new CreateCardRequest(
                "Costco",
                "Citi",
                "Visa",
                BigDecimal.ZERO,
                BigDecimal.valueOf(1.5)
        );

        when(cardRepository.existsByNameIgnoreCaseAndIssuerIgnoreCase(
                request.name(), request.issuer()))
                .thenReturn(false);

        //  Create a separate Card to simulate the saved entity
        Card savedCard = new Card(
                "Costco",
                "Citi",
                "Visa",
                BigDecimal.ZERO,
                BigDecimal.valueOf(1.5)
        );
        when(cardRepository.save(any(Card.class)))
                .thenReturn(savedCard);

        // Act
        Card result = cardService.addCard(request);

        // Assert
        assertSame(savedCard,result);
        //Verify save() was called exactly once
        verify(cardRepository, times(1)).save(any(Card.class));
    }
    @Test
    void deleteCardSuccessfully() {
        // Arrange
        Card costco = new Card(
                "Costco",
                "Citi",
                "Visa",
                BigDecimal.ZERO,
                BigDecimal.valueOf(1.5)
        );

        when(cardRepository.findById(1L))
                .thenReturn(Optional.of(costco));

        // Act
        cardService.deleteCard(1L);

        // Assert
        verify(cardRepository).delete(costco);
    }
    @Test
    void deleteCardThrowsWhenCardNotFound() {
        // Arrange
        when(cardRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act&Assert
        assertThrows(CardNotFoundException.class,()->cardService.deleteCard(99L));
        //Verify
        verify(cardRepository,never()).delete(any(Card.class));

    }
    @Test
    void updateCardSuccessfully() {
        // Arrange
        Card costco = new Card(
                "Costco",
                "Citi",
                "Visa",
                BigDecimal.ZERO,
                BigDecimal.valueOf(1.5)
        );

        UpdateCardRequest request = new UpdateCardRequest(
                "Costco Anywhere",
                "Citi",
                "Visa",
                BigDecimal.valueOf(95),
                BigDecimal.valueOf(2.0),
                false
        );

        when(cardRepository.findById(1L))
                .thenReturn(Optional.of(costco));


        when(cardRepository.save(any(Card.class))).thenAnswer(invocation ->invocation.getArgument(0));

        // Act

        Card result=cardService.updateCard(1L,request);

        // Assert
        assertEquals("Costco Anywhere",result.getName());
        assertEquals("Citi",result.getIssuer());
        assertEquals("Visa",result.getNetwork());
        assertEquals(BigDecimal.valueOf(95),result.getAnnualFee());
        assertEquals(BigDecimal.valueOf(2.0),result.getDefaultCashbackPercent());
        assertFalse(result.isActive());


        verify(cardRepository).save(costco);
    }
    @Test
    void updateCardThrowsWhenCardNotFound(){
        // Arrange
        UpdateCardRequest request = new UpdateCardRequest(
                "Costco Anywhere",
                "Citi",
                "Visa",
                BigDecimal.valueOf(95),
                BigDecimal.valueOf(2.0),
                false
        );

        // findById(99L) returns no card.
        when(cardRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CardNotFoundException.class,()->cardService.updateCard(99L,request));
        verify(cardRepository,never()).save(any(Card.class));
    }
    @Test
    void addRewardSuccessfully() {
        // Arrange
        Card costco = new Card(
                "Costco",
                "Citi",
                "Visa",
                BigDecimal.ZERO,
                BigDecimal.valueOf(1.5)
        );

        RewardRuleRequest request = new RewardRuleRequest(
                "Gas",
                BigDecimal.valueOf(5.0)
        );

        when(cardRepository.findById(1L))
                .thenReturn(Optional.of(costco));

        when(cardRepository.save(any(Card.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        cardService.addReward(1L,request);

        // Assert
        assertEquals(1,costco.getRewardRules().size());
        assertEquals("Gas",costco.getRewardRules().get(0).getCategory());
        assertEquals(BigDecimal.valueOf(5.0),costco.getRewardRules().get(0).getCashbackPercent());
        
        verify(cardRepository).save(costco);
    }
}
