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

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
            doReturn(List.of(card1, card2)).when(cardRepository).findAll();
            assertAll(
                    () -> assertEquals(List.of(card1, card2), cardService.getAll()),
                    () -> assertEquals(2, cardService.getAll().size()),
                    () -> assertNotEquals(List.of(card1, card2, card3), cardService.getAll()),
                    () -> assertNotEquals(List.of(card2, card3), cardService.getAll())
            );
        }

        @Test
        void checkGetAllShouldReturnEmptyList() {
            doReturn(emptyList()).when(cardRepository).findAll();
            assertEquals(emptyList(), cardService.getAll());
        }
    }

    @Nested
    class GetByIdTest {
        @Test
        public void checkGetByIdShouldReturnCard() {
            doReturn(Optional.of(card1)).when(cardRepository).findById(1L);
            doReturn(Optional.of(card3)).when(cardRepository).findById(3L);
            assertAll(
                    () -> assertEquals(card1, cardService.getById(1L)),
                    () -> assertNotEquals(card3, cardService.getById(1L)),
                    () -> assertNotEquals(card1, cardService.getById(3L))
            );
        }

        @Test
        void checkGetByIdShouldThrowEntityNotFoundException() {
            doReturn(Optional.of(card1)).when(cardRepository).findById(1L);
            assertAll(
                    () -> assertDoesNotThrow(() -> cardService.getById(1L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> cardService.getById(0L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> cardService.getById(null))
            );
        }

        @Test
        void checkGetByIdShouldThrowNullPointerException() {
            doReturn(null).when(cardRepository).findById(1L);
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
            doReturn(Optional.of(card1)).when(cardRepository).findByNumber(1111);
            doReturn(Optional.of(card2)).when(cardRepository).findByNumber(1234);
            assertAll(
                    () -> assertEquals(card1, cardService.getByNumber(1111)),
                    () -> assertNotEquals(card1, cardService.getByNumber(1234))
            );
        }

        @Test
        void checkGetByNumberShouldThrowEntityNotFoundException() {
            doReturn(Optional.of(card1)).when(cardRepository).findByNumber(1111);
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
            doReturn(Optional.of(card1)).when(cardRepository).findByNumber(1111);
            assertAll(
                    () -> assertEquals(10.0, cardService.getDiscountByNumber(1111)),
                    () -> assertNotEquals(15.0, cardService.getDiscountByNumber(1111))
            );
        }

        @Test
        void checkGetDiscountByNumberShouldThrowEntityNotFoundException() {
            doReturn(Optional.of(card1)).when(cardRepository).findByNumber(1111);
            assertAll(
                    () -> assertDoesNotThrow(() -> cardService.getDiscountByNumber(1111)),
                    () -> assertThrows(EntityNotFoundException.class, () -> cardService.getDiscountByNumber(1234))
            );
        }
    }
}
