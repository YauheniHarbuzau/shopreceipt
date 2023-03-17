package com.example.shopreceipt.service.util;

import com.example.shopreceipt.entity.Card;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCard")
@With
public class CardTestBuilder implements TestBuilder<Card> {

    private Long id = 0L;
    private Integer number = 0;
    private Double discount = 0.0;

    @Override
    public Card build() {
        final var card = new Card();
        card.setId(id);
        card.setNumber(number);
        card.setDiscount(discount);
        return card;
    }
}
