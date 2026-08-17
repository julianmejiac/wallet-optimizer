package com.julianmejiac.walletoptimizer.controller;

import com.julianmejiac.walletoptimizer.dto.BudgetRequest;
import com.julianmejiac.walletoptimizer.dto.BudgetTotalResponse;
import com.julianmejiac.walletoptimizer.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budget")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping("/recommendation")
    public BudgetTotalResponse recommendBudget(@Valid @RequestBody BudgetRequest budgetRequest){
        return budgetService.calculateBudgetRecommendation(budgetRequest);

    }
}
