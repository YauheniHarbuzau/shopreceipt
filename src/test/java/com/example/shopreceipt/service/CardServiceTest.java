package com.example.shopreceipt.service;

import com.example.shopreceipt.entity.Card;
import com.example.shopreceipt.repository.CardRepository;
import com.example.shopreceipt.service.util.CardTestBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link CardService}
 */
@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @InjectMocks
    private CardService cardService;

    private static Card card1 = new Card();
    private static Card card2 = new Card();
    private static Card card3 = new Card();

    @BeforeEach
    void setUp() {
        card1 = CardTestBuilder.aCard().withId(1L).withNumber(1111).withDiscount(10.0).build();
        card2 = CardTestBuilder.aCard().withId(2L).withNumber(1234).build();
        card3 = CardTestBuilder.aCard().withId(3L).build();
    }

    @Nested
    class GetAllTest {
        @Test
        void checkGetAllShouldCalledRepositoryMethod() {
            cardService.getAll();
            verify(cardRepository).findAll();
        }

        @Test
        void checkGetAllShouldReturnAllCards() {
            when(cardRepository.findAll()).thenReturn(List.of(card1, card2));
            assertAll(
                    () -> assertEquals(List.of(card1, card2), cardService.getAll()),
                    () -> assertEquals(2, cardService.getAll().size()),
                    () -> assertNotEquals(List.of(card1, card2, card3), cardService.getAll()),
                    () -> assertNotEquals(List.of(card2, card3), cardService.getAll())
            );
        }

        @Test
        void checkGetAllShouldReturnEmptyList() {
            when(cardRepository.findAll()).thenReturn(Collections.emptyList());
            assertEquals(Collections.emptyList(), cardService.getAll());
        }
    }

    @Nested
    class GetByIdTest {
        @Test
        public void checkGetByIdShouldReturnCard() {
            when(cardRepository.findById(1L)).thenReturn(Optional.of(card1));
            when(cardRepository.findById(3L)).thenReturn(Optional.of(card3));
            assertAll(
                    () -> assertEquals(card1, cardService.getById(1L)),
                    () -> assertNotEquals(card3, cardService.getById(1L)),
                    () -> assertNotEquals(card1, cardService.getById(3L))
            );
        }

        @Test
        void checkGetByIdShouldThrowEntityNotFoundException() {
            when(cardRepository.findById(1L)).thenReturn(Optional.of(card1));
            assertAll(
                    () -> assertDoesNotThrow(() -> cardService.getById(1L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> cardService.getById(0L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> cardService.getById(null))
            );
        }

        @Test
        void checkGetByIdShouldThrowNullPointerException() {
            when(cardRepository.findById(1L)).thenReturn(null);
            assertThrows(NullPointerException.class, () -> cardService.getById(1L));
        }
    }

    @Test
    void checkSaveShouldCalledRepositoryMethod() {
        cardService.save(card1);
        verify(cardRepository).save(card1);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void checkDeleteByIdShouldCalledRepositoryMethod(long argument) {
        cardService.deleteById(argument);
        verify(cardRepository).deleteById(argument);
    }

    @Nested
    class GetByNumberTest {
        @Test
        void checkGetByNumberShouldReturnCard() {
            when(cardRepository.findByNumber(1111)).thenReturn(Optional.of(card1));
            when(cardRepository.findByNumber(1234)).thenReturn(Optional.of(card2));
            assertAll(
                    () -> assertEquals(card1, cardService.getByNumber(1111)),
                    () -> assertNotEquals(card1, cardService.getByNumber(1234))
            );
        }

        @Test
        void checkGetByNumberShouldThrowEntityNotFoundException() {
            when(cardRepository.findByNumber(1111)).thenReturn(Optional.of(card1));
            assertAll(
                    () -> assertDoesNotThrow(() -> cardService.getByNumber(1111)),
                    () -> assertThrows(EntityNotFoundException.class, () -> cardService.getByNumber(1234))
            );
        }
    }

    @Nested
    class GetDiscountByNumberTest {
        @Test
        void checkGetDiscountByNumberShouldReturnDiscount() {
            when(cardRepository.findByNumber(1111)).thenReturn(Optional.of(card1));
            assertAll(
                    () -> assertEquals(10.0, cardService.getDiscountByNumber(1111)),
                    () -> assertNotEquals(15.0, cardService.getDiscountByNumber(1111))
            );
        }

        @Test
        void checkGetDiscountByNumberShouldThrowEntityNotFoundException() {
            when(cardRepository.findByNumber(1111)).thenReturn(Optional.of(card1));
            assertAll(
                    () -> assertDoesNotThrow(() -> cardService.getDiscountByNumber(1111)),
                    () -> assertThrows(EntityNotFoundException.class, () -> cardService.getDiscountByNumber(1234))
            );
        }
    }
}
