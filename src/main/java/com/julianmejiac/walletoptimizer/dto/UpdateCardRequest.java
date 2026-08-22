package com.julianmejiac.walletoptimizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateCardRequest(
        @NotBlank String name,
        @NotBlank String issuer,
        @NotBlank String network,
        @PositiveOrZero double annualFee,
        @Positive BigDecimal defaultCashbackPercent,
        boolean active
) {}