package com.julianmejiac.walletoptimizer.controller;

import com.julianmejiac.walletoptimizer.dto.BudgetItem;
import com.julianmejiac.walletoptimizer.dto.BudgetRequest;
import com.julianmejiac.walletoptimizer.service.BudgetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BudgetController.class)
public class BudgetControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private BudgetService budgetService;
    @Test
    void shouldReturnBadRequestWhenMonthlyAmountIsNegative() throws Exception {

        mockMvc.perform(post("/budget/recommendation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "expenses": [
                                {
                                  "category": "Gas",
                                  "monthlyAmount": -100
                                }
                              ]
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenMonthlyAmountIsZero() throws Exception {

        mockMvc.perform(post("/budget/recommendation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "expenses": [
                                {
                                  "category": "Gas",
                                  "monthlyAmount": 0
                                }
                              ]
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkWhenMonthlyAmountIsPositive() throws Exception {

        mockMvc.perform(post("/budget/recommendation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "expenses": [
                                {
                                  "category": "Gas",
                                  "monthlyAmount": 50.0
                                }
                              ]
                            }
                            """))
                .andExpect(status().isOk());
        BudgetRequest expectedRequest =
                new BudgetRequest(
                        List.of(
                                new BudgetItem(
                                        "Gas",
                                        new BigDecimal("50.0")
                                )
                        )
                );
        verify(budgetService)
                .calculateBudgetRecommendation(expectedRequest);
    }
}
