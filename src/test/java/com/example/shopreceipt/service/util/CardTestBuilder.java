package com.example.shopreceipt.service.util;

import com.example.shopreceipt.entity.Card;

public class CardTestBuilder implements TestBuilder<Card> {

    private Long id = 0L;
    private Integer number = 0;
    private Double discount = 0.0;

    private CardTestBuilder() {
    }

    public static CardTestBuilder aCard() {
        return new CardTestBuilder();
    }

    public CardTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CardTestBuilder withNumber(Integer number) {
        this.number = number;
        return this;
    }

    public CardTestBuilder withDiscount(Double discount) {
        this.discount = discount;
        return this;
    }

    @Override
    public Card build() {
        final var card = new Card();
        card.setId(id);
        card.setNumber(number);
        card.setDiscount(discount);
        return card;
    }
}