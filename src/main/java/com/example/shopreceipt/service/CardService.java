package com.example.shopreceipt.service;

import com.example.shopreceipt.entity.Card;
import com.example.shopreceipt.exeption.EntityIsNotCorrectException;
import com.example.shopreceipt.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for the {@link Card}
 *
 * @see CardRepository
 */
@AllArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;

    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    public Card getById(Long id) {
        return cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Card not found by id: " + id)
        );
    }

    public void save(Card card) {
        try {
            cardRepository.save(card);
        } catch (RuntimeException ex) {
            throw new EntityIsNotCorrectException("Card entity is not correct or card number already exists");
        }
    }

    public void deleteById(Long id) {
        try {
            cardRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException("Card not found by id: " + id);
        }
    }

    public Card getByNumber(Integer number) {
        return cardRepository.findByNumber(number).orElseThrow(
                () -> new EntityNotFoundException("Card not found by number: " + number)
        );
    }

    public Double getDiscountByNumber(Integer number) {
        return getByNumber(number).getDiscount();
    }
}