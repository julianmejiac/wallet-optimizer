package com.julianmejiac.walletoptimizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateCardRequest(
        @NotBlank String name,
        @NotBlank String issuer,
        @NotBlank String network,
        @NotNull(message = "Annual fee is required")
        @PositiveOrZero(message = "annual fee cannot be negative") BigDecimal annualFee,
        @Positive BigDecimal defaultCashbackPercent,
        boolean active
) {}