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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shopreceipt.constants.Constants.RECEIPT_LIST_1;
import static com.example.shopreceipt.constants.Constants.RECEIPT_LIST_2;
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
        product1 = ProductTestBuilder.aProduct().withId(1L).withName("product 1").withPrice(5.0).withPromotion(false).build();
        product2 = ProductTestBuilder.aProduct().withId(2L).withName("product 2").withPrice(10.0).withPromotion(true).build();
        product3 = ProductTestBuilder.aProduct().withId(3L).withName("product 3").withPrice(15.0).withPromotion(true).build();

        card1 = CardTestBuilder.aCard().withId(1L).withNumber(1234).withDiscount(10.0).build();
        card2 = CardTestBuilder.aCard().withId(2L).withNumber(1111).withDiscount(20.0).build();
    }

    @Nested
    class IsValidTest {
        @Test
        void checkIsValidShouldReturnTrue() {
            assertAll(
                    () -> assertTrue(receiptService.isValid("1-5 2-3 3-1")),
                    () -> assertTrue(receiptService.isValid("1-5 2-3 3-1 card-1234"))
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
        Map<Long, Integer> mapLongInt = new HashMap<>(3);
        mapLongInt.put(1L, 5);
        mapLongInt.put(2L, 3);
        mapLongInt.put(3L, 1);

        when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
        assertAll(
                () -> assertEquals(mapLongInt, receiptService.sourceToMap("1-5 2-3 3-1")),
                () -> assertEquals(mapLongInt, receiptService.sourceToMap("1-5 2-3 3-1 card-1234")),
                () -> assertNotEquals(mapLongInt, receiptService.sourceToMap("1-7 2-3 3-1"))
        );
    }

    @Test
    void checkGetPriceMapShouldReturnMap() {
        Map<String, Double> priceMap = new HashMap<>(3);
        priceMap.put("product 1", 25.0);
        priceMap.put("product 2", 30.0);
        priceMap.put("product 3", 15.0);

        when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
        when(productService.getProductName(1L)).thenReturn(product1.getName());
        when(productService.getProductName(2L)).thenReturn(product2.getName());
        when(productService.getProductName(3L)).thenReturn(product3.getName());
        when(productService.getProductPrice(1L)).thenReturn(product1.getPrice());
        when(productService.getProductPrice(2L)).thenReturn(product2.getPrice());
        when(productService.getProductPrice(3L)).thenReturn(product3.getPrice());
        assertAll(
                () -> assertEquals(priceMap, receiptService.getPriceMap("1-5 2-3 3-1")),
                () -> assertEquals(priceMap, receiptService.getPriceMap("1-5 2-3 3-1 card-1234")),
                () -> assertNotEquals(priceMap, receiptService.getPriceMap("1-7 2-3 3-1"))
        );
    }

    @Test
    void checkGetAmountMapShouldReturnMap() {
        Map<String, Integer> amountMap = new HashMap<>(3);
        amountMap.put("product 1", 5);
        amountMap.put("product 2", 3);
        amountMap.put("product 3", 1);

        when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
        when(productService.getProductName(1L)).thenReturn(product1.getName());
        when(productService.getProductName(2L)).thenReturn(product2.getName());
        when(productService.getProductName(3L)).thenReturn(product3.getName());
        assertAll(
                () -> assertEquals(amountMap, receiptService.getAmountMap("1-5 2-3 3-1")),
                () -> assertNotEquals(amountMap, receiptService.getAmountMap("1-7 2-3 3-1"))
        );
    }

    @Test
    void checkCountPromotionProductShouldReturnCount() {
        Map<Long, Integer> mapLongInt = new HashMap<>(3);
        mapLongInt.put(1L, 5);
        mapLongInt.put(2L, 3);
        mapLongInt.put(3L, 1);

        when(productService.getProductPromotion(1L)).thenReturn(product1.getPromotion());
        when(productService.getProductPromotion(2L)).thenReturn(product2.getPromotion());
        when(productService.getProductPromotion(3L)).thenReturn(product3.getPromotion());
        assertAll(
                () -> assertEquals(2, receiptService.countPromotionProduct(mapLongInt)),
                () -> assertNotEquals(3, receiptService.countPromotionProduct(mapLongInt))
        );
    }

    @Test
    void checkGetFullPriceShouldReturnPrice() {
        Map<String, Double> priceMap = new HashMap<>(3);
        priceMap.put("product 1", 25.0);
        priceMap.put("product 2", 30.0);
        priceMap.put("product 3", 15.0);

        assertAll(
                () -> assertEquals(70.0, receiptService.getFullPrice(priceMap)),
                () -> assertNotEquals(70.1, receiptService.getFullPrice(priceMap))
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
                () -> assertEquals(10.0, receiptService.getCardDiscount("1-5 2-3 3-1 card-1234")),
                () -> assertNotEquals(15.0, receiptService.getCardDiscount("1-5 2-3 3-1 card-1234"))
        );
    }

    @Test
    void checkGetTotalPriceShouldReturnPrice() {
        Map<String, Double> priceMap = new HashMap<>(3);
        priceMap.put("product 1", 25.0);
        priceMap.put("product 2", 30.0);
        priceMap.put("product 3", 15.0);

        when(cardService.getAll()).thenReturn(List.of(card1, card2));
        when(cardService.getDiscountByNumber(1234)).thenReturn(10.0);
        assertAll(
                () -> assertEquals(63.0, receiptService.getTotalPrice(priceMap, "1-5 2-3 3-1 card-1234")),
                () -> assertNotEquals(63.1, receiptService.getTotalPrice(priceMap, "1-5 2-3 3-1 card-1234")),
                () -> assertEquals(70.0, receiptService.getTotalPrice(priceMap, "1-5 2-3 3-1")),
                () -> assertNotEquals(70.1, receiptService.getTotalPrice(priceMap, "1-5 2-3 3-1"))
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
                () -> assertEquals(RECEIPT_LIST_1, receiptService.generateFullReceipt("1-5 2-3 3-1")),
                () -> assertEquals(RECEIPT_LIST_2, receiptService.generateFullReceipt("1-5 2-3 3-1 card-1234"))
        );
    }
}
