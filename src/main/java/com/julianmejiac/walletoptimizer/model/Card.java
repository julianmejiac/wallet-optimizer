package com.julianmejiac.walletoptimizer.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String issuer;
    @NotBlank
    private String network;
    @PositiveOrZero
    private double annualFee;
    private Boolean active;
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<RewardRule> rewardRules;
    public Card(){
        this.active=true;
        this.rewardRules=new ArrayList<>();
    }

    public Card(String name, String issuer, String network, double annualFee) {
        this();
        this.name = name;
        this.issuer = issuer;
        this.network = network;
        this.annualFee = annualFee;
                    }

    public void addRewardRule(RewardRule rewardRule){

        rewardRules.add(rewardRule);
        rewardRule.setCard(this);
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public double getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(double annualFee) {
        this.annualFee = annualFee;
    }

    public List<RewardRule> getRewardRules() {
        return rewardRules;
    }

    public void setRewardRules(List<RewardRule> rewardRules) {
        this.rewardRules = rewardRules;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user='" + issuer + '\'' +
                ", network='" + network + '\'' +
                ", annualFee=" + annualFee +
                ", active=" + active +
                '}';
    }
}
