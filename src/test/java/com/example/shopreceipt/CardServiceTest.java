package com.example.shopreceipt;

import com.example.shopreceipt.entity.Card;
import com.example.shopreceipt.repository.CardRepository;
import com.example.shopreceipt.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link CardService}
 */
@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private static final Card card1 = new Card();
    private static final Card card2 = new Card();
    private static final Card card3 = new Card();

    @BeforeEach
    public void setup() {
        card1.setId(1L);
        card1.setNumber(1111);
        card1.setDiscount(10.0);
        card2.setId(2L);
        card3.setId(3L);
    }

    @Test
    @DisplayName("getAll test")
    public void getAll() {
        when(cardRepository.findAll()).thenReturn(List.of(card1, card2));
        assertEquals(List.of(card1, card2), cardService.getAll());
        assertNotEquals(List.of(card1, card2, card3), cardService.getAll());
        assertNotEquals(List.of(card2, card3), cardService.getAll());
    }

    @Test
    @DisplayName("getById test")
    public void getById() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card1));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(card2));
        when(cardRepository.findById(3L)).thenReturn(Optional.of(card3));
        assertEquals(card1, cardService.getById(1L));
        assertEquals(card2, cardService.getById(2L));
        assertEquals(card3, cardService.getById(3L));
        assertNotEquals(card2, cardService.getById(1L));
        assertNotEquals(card3, cardService.getById(1L));
    }

    @Test
    @DisplayName("save test")
    public void save() {
        cardService.save(card1);
        verify(cardRepository).save(card1);
    }

    @Test
    @DisplayName("deleteById test")
    public void deleteById() {
        cardService.deleteById(1L);
        verify(cardRepository).deleteById(1L);
    }

    @Test
    @DisplayName("getByNumber test")
    public void getByNumber() {
        when(cardRepository.findByNumber(1111)).thenReturn(Optional.of(card1));
        assertEquals(card1, cardService.getByNumber(1111));
    }

    @Test
    @DisplayName("getDiscountByNumber test")
    public void getDiscountByNumber() {
        when(cardRepository.findByNumber(1111)).thenReturn(Optional.of(card1));
        assertEquals(10.0, cardService.getDiscountByNumber(1111));
    }
}