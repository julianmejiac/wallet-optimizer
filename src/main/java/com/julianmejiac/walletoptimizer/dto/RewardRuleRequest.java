package com.julianmejiac.walletoptimizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record RewardRuleRequest(
        @NotBlank(message = "Category is required") String category,
        @Positive(message = "Cashback should be 0 or positive") double cashbackPercent) {

}
