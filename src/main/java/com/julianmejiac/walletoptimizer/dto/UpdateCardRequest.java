package com.julianmejiac.walletoptimizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateCardRequest(
        @NotBlank String name,
        @NotBlank String issuer,
        @NotBlank String network,
        @PositiveOrZero double annualFee,
        boolean active
) {}