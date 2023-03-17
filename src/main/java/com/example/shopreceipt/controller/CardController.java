package com.example.shopreceipt.controller;

import com.example.shopreceipt.entity.Card;
import com.example.shopreceipt.service.CardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for the {@link Card}
 *
 * @see CardService
 */
@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/example")
public class CardController {

    private final CardService cardService;

    @GetMapping("/cards")
    public List<Card> getAll() {
        return cardService.getAll();
    }

    @GetMapping("/cards/{id}")
    public Card getById(@PathVariable("id") @NotNull Long id) {
        return cardService.getById(id);
    }

    @PostMapping("/cards")
    public void save(@RequestBody @Valid Card card) {
        cardService.save(card);
    }

    @DeleteMapping("/cards/{id}")
    public void deleteById(@PathVariable("id") @NotNull Long id) {
        cardService.deleteById(id);
    }
}
