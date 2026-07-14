package com.julianmejiac.walletoptimizer.service;

import com.julianmejiac.walletoptimizer.exception.CardNotFoundException;
import com.julianmejiac.walletoptimizer.model.Card;
import com.julianmejiac.walletoptimizer.model.RewardRule;
import com.julianmejiac.walletoptimizer.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    private final CardRepository cardRepository;
    public CardService(CardRepository cardRepository){
        this.cardRepository=cardRepository;
    }
    public List<Card> getAllCards(){
        return cardRepository.findAll();
    }


    public List<Card> getCardsByName(String name){
        List<Card> matchingCards=new ArrayList<>();
        for(Card card: cardRepository.findAll()){
            if (card.getName().equalsIgnoreCase(name)){
                matchingCards.add(card);
            }
        }
        return matchingCards;
    }
    public Card  addCard(Card card){
        return cardRepository.save(card);
    }




    public Card getCardById(Long cardId){
        Optional<Card> optionalCard=cardRepository.findById(cardId);
        if (optionalCard.isPresent()){
            return optionalCard.get();
        }
        throw new CardNotFoundException("Card not found with id "+cardId);
    }



    public Card addReward(Long cardId,RewardRule rewardRule){
        Card card=getCardById(cardId);
        card.addRewardRule(rewardRule);
        return cardRepository.save(card);
    }

    public List<Card> recommendCard(String category){
        List<Card> bestCards=new ArrayList<>();
        double bestReward=0.0;
        for (Card card :cardRepository.findAll()){
            for(RewardRule rewardRule:card.getRewardRules()){
                double cashback=rewardRule.getCashbackPercent();
                if (rewardRule.getCategory().equalsIgnoreCase(category)){
                    if(bestReward==cashback){
                        bestCards.add(card);
                    }
                    else if(bestReward<cashback){
                        bestCards.clear();
                        bestCards.add(card);
                        bestReward=cashback;
                    }


                }

            }

        }

        return bestCards;
    }

}
