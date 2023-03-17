package com.example.shopreceipt.repository;

import com.example.shopreceipt.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for the {@link Card}
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Override
    Optional<Card> findById(Long id);

    Optional<Card> findByNumber(Integer number);
}
