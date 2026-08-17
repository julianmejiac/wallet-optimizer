package com.julianmejiac.walletoptimizer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record BudgetRequest(@NotEmpty List<@Valid BudgetItem> expenses) {
}
