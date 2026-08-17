package com.julianmejiac.walletoptimizer.service;

import com.julianmejiac.walletoptimizer.dto.*;
import com.julianmejiac.walletoptimizer.exception.CardNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service public class BudgetService {
    private final CardService cardService;

    public BudgetService(CardService cardService) {
        this.cardService = cardService;
    }

    public BudgetTotalResponse calculateBudgetRecommendation(
            BudgetRequest budgetRequest) {



        List<BudgetRecommendation> recommendations =new ArrayList<>();
        BigDecimal totalMonthlyExpenses= BigDecimal.ZERO;
        BigDecimal totalMonthlyRewards= BigDecimal.ZERO;

        for (BudgetItem budgetItem:budgetRequest.expenses()){
            String category=budgetItem.category();
            BigDecimal monthlyAmount=budgetItem.monthlyAmount();
            List<CardRecommendationDTO> optimalCards=cardService.recommendCard(category);
            if (optimalCards.isEmpty()) {
                throw new CardNotFoundException("Category "+ category+ " does not have a card");
            }

            List<String> cardNames=new ArrayList<>();

            for(CardRecommendationDTO card:optimalCards){
                cardNames.add(card.cardName());
                            }
            BigDecimal cashbackPercent=optimalCards.get(0).cashbackPercent();
            BigDecimal monthlyReward=(monthlyAmount.multiply(cashbackPercent)).divide(BigDecimal.valueOf(100));

            BudgetRecommendation recommendation=new BudgetRecommendation(category,monthlyAmount,cardNames,cashbackPercent,monthlyReward);
            recommendations.add(recommendation);
            totalMonthlyExpenses=totalMonthlyExpenses.add(monthlyAmount);
            totalMonthlyRewards=totalMonthlyRewards.add(monthlyReward);

        }
        BigDecimal totalAnnualRewards=totalMonthlyRewards.multiply(BigDecimal.valueOf(12));
        BudgetTotalResponse budgetTotalResponse=new BudgetTotalResponse(recommendations,totalMonthlyExpenses,totalMonthlyRewards,totalAnnualRewards);
        return budgetTotalResponse;
    }
}
