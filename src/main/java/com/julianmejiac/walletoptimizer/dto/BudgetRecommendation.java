package com.julianmejiac.walletoptimizer.dto;

import java.math.BigDecimal;
import java.util.List;

public record BudgetRecommendation(
        String category,
        BigDecimal monthlyAmount,
        List<String> cardNames,
        BigDecimal cashbackPercent,
        BigDecimal monthlyReward
) {}

