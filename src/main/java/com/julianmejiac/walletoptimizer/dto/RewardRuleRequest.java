package com.julianmejiac.walletoptimizer.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RewardRuleRequest(
        @NotBlank(message = "Category is required") String category,
        @NotNull(message = "Cashback percentage is required")
        @DecimalMax(value = "100.0",
                message = "Cashback percentage cannot exceed 100")
        @Positive(message = "Cashback should be positive") BigDecimal cashbackPercent) {

}
