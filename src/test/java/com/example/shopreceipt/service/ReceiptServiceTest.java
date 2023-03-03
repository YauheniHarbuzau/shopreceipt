package com.example.shopreceipt.service;

import com.example.shopreceipt.entity.Card;
import com.example.shopreceipt.entity.Product;
import com.example.shopreceipt.service.util.CardTestBuilder;
import com.example.shopreceipt.service.util.ProductTestBuilder;
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

import static com.example.shopreceipt.service.constants.TestConstants.AMOUNT_MAP;
import static com.example.shopreceipt.service.constants.TestConstants.MAP_LONG_INT;
import static com.example.shopreceipt.service.constants.TestConstants.PRICE_MAP;
import static com.example.shopreceipt.service.constants.TestConstants.PRODUCT_NAME_1;
import static com.example.shopreceipt.service.constants.TestConstants.PRODUCT_NAME_2;
import static com.example.shopreceipt.service.constants.TestConstants.PRODUCT_NAME_3;
import static com.example.shopreceipt.service.constants.TestConstants.RECEIPT_LIST_1;
import static com.example.shopreceipt.service.constants.TestConstants.RECEIPT_LIST_2;
import static com.example.shopreceipt.service.constants.TestConstants.SOURCE_LINE_1;
import static com.example.shopreceipt.service.constants.TestConstants.SOURCE_LINE_2;
import static com.example.shopreceipt.service.constants.TestConstants.SOURCE_LINE_3;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link ReceiptService}
 */
@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @Mock
    private ProductService productService;
    @Mock
    private CardService cardService;
    @InjectMocks
    private ReceiptService receiptService;

    private static Product product1 = new Product();
    private static Product product2 = new Product();
    private static Product product3 = new Product();

    private static Card card1 = new Card();
    private static Card card2 = new Card();

    @BeforeEach
    void setUp() {
        product1 = ProductTestBuilder.aProduct().withId(1L).withName(PRODUCT_NAME_1).withPrice(5.0).withPromotion(false).build();
        product2 = ProductTestBuilder.aProduct().withId(2L).withName(PRODUCT_NAME_2).withPrice(10.0).withPromotion(true).build();
        product3 = ProductTestBuilder.aProduct().withId(3L).withName(PRODUCT_NAME_3).withPrice(15.0).withPromotion(true).build();

        card1 = CardTestBuilder.aCard().withId(1L).withNumber(1234).withDiscount(10.0).build();
        card2 = CardTestBuilder.aCard().withId(2L).withNumber(1111).withDiscount(20.0).build();

        MAP_LONG_INT.put(1L, 5);
        MAP_LONG_INT.put(2L, 3);
        MAP_LONG_INT.put(3L, 1);

        PRICE_MAP.put(PRODUCT_NAME_1, 25.0);
        PRICE_MAP.put(PRODUCT_NAME_2, 30.0);
        PRICE_MAP.put(PRODUCT_NAME_3, 15.0);

        AMOUNT_MAP.put(PRODUCT_NAME_1, 5);
        AMOUNT_MAP.put(PRODUCT_NAME_2, 3);
        AMOUNT_MAP.put(PRODUCT_NAME_3, 1);
    }

    @Nested
    class IsValidTest {
        @Test
        void checkIsValidShouldReturnTrue() {
            assertAll(
                    () -> assertTrue(receiptService.isValid(SOURCE_LINE_1)),
                    () -> assertTrue(receiptService.isValid(SOURCE_LINE_2))
            );
        }

        @Test
        void checkIsValidShouldReturnFalse() {
            assertAll(
                    () -> assertFalse(receiptService.isValid("is not valid")),
                    () -> assertFalse(receiptService.isValid("1-32-1 3-1"))
            );
        }
    }

    @Nested
    class ProductIsPresentTest {
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkProductIsPresentShouldReturnTrue(long argument) {
            when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
            assertTrue(receiptService.productIsPresent(argument));
        }

        @Test
        void checkProductIsPresentShouldReturnFalse() {
            when(productService.getAll()).thenReturn(List.of(product1, product2));
            assertFalse(receiptService.productIsPresent(3L));
        }
    }

    @Test
    void checkSourceToMapShouldReturnMap() {
        when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
        assertAll(
                () -> assertEquals(MAP_LONG_INT, receiptService.sourceToMap(SOURCE_LINE_1)),
                () -> assertEquals(MAP_LONG_INT, receiptService.sourceToMap(SOURCE_LINE_2)),
                () -> assertNotEquals(MAP_LONG_INT, receiptService.sourceToMap(SOURCE_LINE_3))
        );
    }

    @Test
    void checkGetPriceMapShouldReturnMap() {
        when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
        when(productService.getProductName(1L)).thenReturn(product1.getName());
        when(productService.getProductName(2L)).thenReturn(product2.getName());
        when(productService.getProductName(3L)).thenReturn(product3.getName());
        when(productService.getProductPrice(1L)).thenReturn(product1.getPrice());
        when(productService.getProductPrice(2L)).thenReturn(product2.getPrice());
        when(productService.getProductPrice(3L)).thenReturn(product3.getPrice());
        assertAll(
                () -> assertEquals(PRICE_MAP, receiptService.getPriceMap(SOURCE_LINE_1)),
                () -> assertEquals(PRICE_MAP, receiptService.getPriceMap(SOURCE_LINE_2)),
                () -> assertNotEquals(PRICE_MAP, receiptService.getPriceMap(SOURCE_LINE_3))
        );
    }

    @Test
    void checkGetAmountMapShouldReturnMap() {
        when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
        when(productService.getProductName(1L)).thenReturn(product1.getName());
        when(productService.getProductName(2L)).thenReturn(product2.getName());
        when(productService.getProductName(3L)).thenReturn(product3.getName());
        assertAll(
                () -> assertEquals(AMOUNT_MAP, receiptService.getAmountMap(SOURCE_LINE_1)),
                () -> assertNotEquals(AMOUNT_MAP, receiptService.getAmountMap(SOURCE_LINE_3))
        );
    }

    @Test
    void checkCountPromotionProductShouldReturnCount() {
        when(productService.getProductPromotion(1L)).thenReturn(product1.getPromotion());
        when(productService.getProductPromotion(2L)).thenReturn(product2.getPromotion());
        when(productService.getProductPromotion(3L)).thenReturn(product3.getPromotion());
        assertAll(
                () -> assertEquals(2, receiptService.countPromotionProduct(MAP_LONG_INT)),
                () -> assertNotEquals(3, receiptService.countPromotionProduct(MAP_LONG_INT))
        );
    }

    @Test
    void checkGetFullPriceShouldReturnPrice() {
        assertAll(
                () -> assertEquals(70.0, receiptService.getFullPrice(PRICE_MAP)),
                () -> assertNotEquals(70.1, receiptService.getFullPrice(PRICE_MAP))
        );
    }

    @Nested
    class CardIsPresentTest {
        @Test
        void checkCardIsPresentShouldReturnTrue() {
            when(cardService.getAll()).thenReturn(List.of(card1, card2));
            assertTrue(receiptService.cardIsPresent(1234));
        }

        @Test
        void checkCardIsPresentShouldReturnFalse() {
            when(cardService.getAll()).thenReturn(List.of(card1, card2));
            assertFalse(receiptService.cardIsPresent(3333));
        }
    }

    @Test
    void checkGetCardDiscountShouldReturnDiscount() {
        when(cardService.getAll()).thenReturn(List.of(card1, card2));
        when(cardService.getDiscountByNumber(1234)).thenReturn(10.0);
        assertAll(
                () -> assertEquals(10.0, receiptService.getCardDiscount(SOURCE_LINE_2)),
                () -> assertNotEquals(15.0, receiptService.getCardDiscount(SOURCE_LINE_2))
        );
    }

    @Test
    void checkGetTotalPriceShouldReturnPrice() {
        when(cardService.getAll()).thenReturn(List.of(card1, card2));
        when(cardService.getDiscountByNumber(1234)).thenReturn(10.0);
        assertAll(
                () -> assertEquals(63.0, receiptService.getTotalPrice(PRICE_MAP, SOURCE_LINE_2)),
                () -> assertNotEquals(63.1, receiptService.getTotalPrice(PRICE_MAP, SOURCE_LINE_2)),
                () -> assertEquals(70.0, receiptService.getTotalPrice(PRICE_MAP, SOURCE_LINE_1)),
                () -> assertNotEquals(70.1, receiptService.getTotalPrice(PRICE_MAP, SOURCE_LINE_1))
        );
    }

    @Test
    void checkGenerateFullReceiptShouldReturnList() {
        when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
        when(productService.getProductName(1L)).thenReturn(product1.getName());
        when(productService.getProductName(2L)).thenReturn(product2.getName());
        when(productService.getProductName(3L)).thenReturn(product3.getName());
        when(productService.getProductPrice(1L)).thenReturn(product1.getPrice());
        when(productService.getProductPrice(2L)).thenReturn(product2.getPrice());
        when(productService.getProductPrice(3L)).thenReturn(product3.getPrice());
        when(cardService.getAll()).thenReturn(List.of(card1, card2));
        when(cardService.getDiscountByNumber(1234)).thenReturn(10.0);
        assertAll(
                () -> assertEquals(RECEIPT_LIST_1, receiptService.generateFullReceipt(SOURCE_LINE_1)),
                () -> assertEquals(RECEIPT_LIST_2, receiptService.generateFullReceipt(SOURCE_LINE_2))
        );
    }
}