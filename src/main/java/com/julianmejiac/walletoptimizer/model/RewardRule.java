package com.julianmejiac.walletoptimizer.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
@Entity
public class RewardRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String category;
    @Min(0)
    @Max(100)
    private double cashbackPercent;
    @ManyToOne
    @JoinColumn(name="card_id")
    @JsonBackReference
    private Card card;

    public RewardRule() {
    }

    public RewardRule(String category, double cashbackPercent) {
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

    public double getCashbackPercent() {
        return cashbackPercent;
    }

    public void setCashbackPercent(double cashbackPercent) {
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
