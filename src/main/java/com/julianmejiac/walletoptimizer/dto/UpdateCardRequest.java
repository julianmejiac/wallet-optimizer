package com.julianmejiac.walletoptimizer.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateCardRequest(
        @NotBlank String name,
        @NotBlank String issuer,
        @NotBlank String network,
        @NotNull(message = "Annual fee is required")
        @PositiveOrZero(message = "annual fee cannot be negative") BigDecimal annualFee,
        @Positive
        @DecimalMax(value = "100.0",
                message = "Cashback percentage cannot exceed 100")
        BigDecimal defaultCashbackPercent,
        boolean active
) {}