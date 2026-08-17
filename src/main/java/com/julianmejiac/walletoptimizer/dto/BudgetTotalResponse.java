package com.julianmejiac.walletoptimizer.dto;

import java.math.BigDecimal;
import java.util.List;

public record BudgetTotalResponse(
        List<BudgetRecommendation> recommendations,
        BigDecimal totalMonthlyExpenses,
        BigDecimal totalMonthlyRewards,
        BigDecimal totalAnnualRewards) {
}
