package com.julianmejiac.walletoptimizer.repository;

import com.julianmejiac.walletoptimizer.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card,Long> {

}
