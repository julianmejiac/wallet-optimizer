package com.julianmejiac.walletoptimizer.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
public class RewardRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String category;
    private BigDecimal cashbackPercent;
    @ManyToOne
    @JoinColumn(name="card_id")
    @JsonBackReference
    private Card card;

    public RewardRule() {
    }

    public RewardRule(String category, BigDecimal cashbackPercent) {
        this.category = category;
        this.cashbackPercent = cashbackPercent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getCashbackPercent() {
        return cashbackPercent;
    }

    public void setCashbackPercent(BigDecimal cashbackPercent) {
        this.cashbackPercent = cashbackPercent;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RewardRule{" +
                "category='" + category + '\'' +
                ", cashbackPercent=" + cashbackPercent +
                '}';
    }
}
