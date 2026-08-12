package com.julianmejiac.walletoptimizer.controller;

import com.julianmejiac.walletoptimizer.dto.CardRecommendationDTO;
import com.julianmejiac.walletoptimizer.dto.CreateCardRequest;
import com.julianmejiac.walletoptimizer.dto.RewardRuleRequest;
import com.julianmejiac.walletoptimizer.dto.UpdateCardRequest;
import com.julianmejiac.walletoptimizer.model.Card;
import com.julianmejiac.walletoptimizer.model.RewardRule;
import com.julianmejiac.walletoptimizer.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.basic.BasicDesktopIconUI;
import java.util.List;

@RestController
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
    @GetMapping("/cards")
    public List<Card> getCards(){
        return cardService.getAllCards();

    }
    @GetMapping("/cards/{cardId}")
    public Card getCardByIndex(@PathVariable Long cardId){
        return cardService.getCardById(cardId);
    }

    @GetMapping("/cards/{cardId}/reward-rules")
    public List<RewardRule> getRewardRulesOfCard(@PathVariable Long cardId){
        return cardService.getCardById(cardId).getRewardRules();
    }
    @GetMapping("/recommend")
    public List<CardRecommendationDTO> recommendCard(@RequestParam String category){
        return cardService.recommendCard(category);
    }
    @GetMapping("/cards/search")
    public List<Card> getCardsByName(@RequestParam String name){
        return cardService.getCardsByName(name);
    }
    @PostMapping("/cards")
    public Card addCard(@Valid @RequestBody CreateCardRequest cardRequest){
        return cardService.addCard(cardRequest);
    }
    @PostMapping("/cards/{cardId}/reward-rules")
    public Card addReward(@PathVariable Long cardId,@Valid @RequestBody RewardRuleRequest rewardRuleRequest){
        return cardService.addReward(cardId,rewardRuleRequest);

    }
    @PutMapping("/cards/{cardId}")
    public Card updateCard(@PathVariable Long cardId, @Valid @RequestBody UpdateCardRequest updatedCardRequest){
        return cardService.updateCard(cardId, updatedCardRequest);
    }
    @PutMapping("/cards/{cardId}/reward-rules/{rewardId}")
    public RewardRule updateReward(@PathVariable Long cardId, @PathVariable Long rewardId, @Valid @RequestBody RewardRuleRequest updatedRuleRequest){
        return cardService.updateReward(cardId,rewardId, updatedRuleRequest);
    }
    @DeleteMapping("/cards/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@PathVariable Long cardId){
        cardService.deleteCard(cardId);
    }
    @DeleteMapping("/cards/{cardId}/reward-rules/{rewardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReward(@PathVariable Long cardId, @PathVariable Long rewardId){
        cardService.deleteReward(cardId,rewardId);
    }



}
