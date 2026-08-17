package com.julianmejiac.walletoptimizer.dto;

import java.math.BigDecimal;

public record CardRecommendationDTO (
        String cardName,
        BigDecimal cashbackPercent
)
{}
