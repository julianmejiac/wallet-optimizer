package com.julianmejiac.walletoptimizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateCardRequest(
        @NotBlank(message = "Card name is required") String name,
        @NotBlank(message = "Issuer is required") String issuer,
        @NotBlank(message="Network is required") String network,
        @PositiveOrZero(message="Annual fee cannot be negative") double annualFee
) {
}
