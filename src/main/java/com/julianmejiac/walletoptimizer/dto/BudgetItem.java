package com.julianmejiac.walletoptimizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


import java.math.BigDecimal;

public record BudgetItem(@NotBlank String category, @NotNull @Positive BigDecimal monthlyAmount) {
}
