package com.julianmejiac.walletoptimizer.service;

import com.julianmejiac.walletoptimizer.dto.*;
import com.julianmejiac.walletoptimizer.repository.CardRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {
    @Mock
    private CardService cardService;
    @InjectMocks
    private BudgetService budgetService;
    @Test
    void calculateBudgetRecommendationTest() {

        // Arrange
        BudgetRequest budgetRequest = new BudgetRequest(List.of(
                new BudgetItem("Gas", BigDecimal.valueOf(100)),
                new BudgetItem("Restaurants", BigDecimal.valueOf(300)),
                new BudgetItem("Groceries", BigDecimal.valueOf(400))
        ));

        when(cardService.recommendCard("Gas"))
                .thenReturn(List.of(
                        new CardRecommendationDTO("Costco", BigDecimal.valueOf(3.0)),
                        new CardRecommendationDTO("SamsClub", BigDecimal.valueOf(3.0))
                ));

        when(cardService.recommendCard("Restaurants"))
                .thenReturn(List.of(
                        new CardRecommendationDTO("Amazon", BigDecimal.valueOf(2.0))
                ));

        when(cardService.recommendCard("Groceries"))
                .thenReturn(List.of(
                        new CardRecommendationDTO("Venture", BigDecimal.valueOf(1.5)),
                        new CardRecommendationDTO("Amazon", BigDecimal.valueOf(1.5)),
                        new CardRecommendationDTO("Kemba", BigDecimal.valueOf(1.5))
                ));

        // Act
        BudgetTotalResponse result =
                budgetService.calculateBudgetRecommendation(budgetRequest);

        // Assert
        BudgetRecommendation gasRecommendation =
                new BudgetRecommendation(
                        "Gas",
                        BigDecimal.valueOf(100),
                        List.of("Costco", "SamsClub"),
                        BigDecimal.valueOf(3.0),
                        BigDecimal.valueOf(3.0)
                );

        BudgetRecommendation restaurantRecommendation =
                new BudgetRecommendation(
                        "Restaurants",
                        BigDecimal.valueOf(300),
                        List.of("Amazon"),
                        BigDecimal.valueOf(2.0),
                        BigDecimal.valueOf(6.0)
                );

        BudgetRecommendation groceryRecommendation =
                new BudgetRecommendation(
                        "Groceries",
                        BigDecimal.valueOf(400),
                        List.of("Venture", "Amazon", "Kemba"),
                        BigDecimal.valueOf(1.5),
                        BigDecimal.valueOf(6.0)
                );

        BudgetTotalResponse expected =
                new BudgetTotalResponse(
                        List.of(
                                gasRecommendation,
                                restaurantRecommendation,
                                groceryRecommendation
                        ),
                        BigDecimal.valueOf(800),
                        BigDecimal.valueOf(15.0),
                        BigDecimal.valueOf(180.0)
                );

        assertEquals(expected, result);
    }
}
