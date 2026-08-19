package com.julianmejiac.walletoptimizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record BudgetRecommendation(
        @NotBlank String category,
        @Positive BigDecimal monthlyAmount,
        List<String> cardNames,
        @Positive BigDecimal cashbackPercent,
        BigDecimal monthlyReward
) {}

