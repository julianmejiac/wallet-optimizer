package com.julianmejiac.walletoptimizer.controller;

import com.julianmejiac.walletoptimizer.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService;

    @Test
    void shouldReturnBadRequestWhenRewardCashbackIsZero() throws Exception {
        mockMvc.perform(post("/cards/1/reward-rules").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "category" :"Gas",
                        "cashbackPercent" :0
                        }
                        """

                )).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cashbackPercent").value("Cashback should be positive"));

    }

    @Test
    void shouldReturnBadRequestWhenRewardCashbackIsNull() throws Exception{
        mockMvc.perform(post("/cards/1/reward-rules").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "category" : "Gas",
                        "cashbackPercent" : null
                        }
                        
                        """
                                ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cashbackPercent").value("Cashback percentage is required"));

    }

    @Test
    void shouldReturnBadRequestWhenRewardCashbackExceeds100() throws Exception {

        mockMvc.perform(post("/cards/1/reward-rules").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "category": "Gas",
                        "cashbackPercent" :120
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cashbackPercent").value("Cashback percentage cannot exceed 100"));


    }

}