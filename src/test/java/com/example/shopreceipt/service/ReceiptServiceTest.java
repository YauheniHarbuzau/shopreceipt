package com.example.shopreceipt.service;

import com.example.shopreceipt.entity.Card;
import com.example.shopreceipt.entity.Product;
import com.example.shopreceipt.entity.Receipt;
import com.example.shopreceipt.entity.ReceiptProduct;
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

import static com.example.shopreceipt.constants.Constants.RECEIPT_LINE_LIST;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
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
    private static Product product4 = new Product();
    private static Product product5 = new Product();
    private static Product product6 = new Product();

    private static Card card1 = new Card();
    private static Card card2 = new Card();

    @BeforeEach
    void setUp() {
        product1 = ProductTestBuilder.aProduct().withId(1L).withName("product1").withPrice(2.0).withPromotion(true).build();
        product2 = ProductTestBuilder.aProduct().withId(2L).withName("product2").withPrice(3.0).withPromotion(true).build();
        product3 = ProductTestBuilder.aProduct().withId(3L).withName("product3").withPrice(4.0).withPromotion(true).build();
        product4 = ProductTestBuilder.aProduct().withId(4L).withName("product4").withPrice(5.0).withPromotion(true).build();
        product5 = ProductTestBuilder.aProduct().withId(5L).withName("product5").withPrice(6.0).withPromotion(true).build();
        product6 = ProductTestBuilder.aProduct().withId(6L).withName("product6").withPrice(7.0).withPromotion(false).build();

        card1 = CardTestBuilder.aCard().withNumber(1234).withDiscount(10.0).build();
        card2 = CardTestBuilder.aCard().withNumber(1111).withDiscount(20.0).build();
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

    @Nested
    class CardIsPresentTest {
        @Test
        void checkCardIsPresentShouldReturnTrue() {
            when(cardService.getAll()).thenReturn(List.of(card1, card2));
            assertAll(
                    () -> assertTrue(receiptService.cardIsPresent(1234)),
                    () -> assertTrue(receiptService.cardIsPresent(1111))
            );
        }

        @Test
        void checkCardIsPresentShouldReturnFalse() {
            when(cardService.getAll()).thenReturn(List.of(card1, card2));
            assertFalse(receiptService.cardIsPresent(3333));
        }
    }

    @Test
    void checkSourceToMapShouldReturnCorrectMap() {
        Map<Long, Integer> expectedMap = new HashMap<>(3);
        expectedMap.put(1L, 5);
        expectedMap.put(2L, 3);
        expectedMap.put(3L, 1);

        when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
        assertAll(
                () -> assertEquals(expectedMap, receiptService.sourceToMap("1-5 2-3 3-1")),
                () -> assertEquals(expectedMap, receiptService.sourceToMap("1-5 2-3 3-1 card-1234")),
                () -> assertNotEquals(expectedMap, receiptService.sourceToMap("1-7 2-3 3-1"))
        );
    }

    @Test
    void getReceiptProductsShouldReturnListWithCorrectSize() {
        doReturn(List.of(product1, product2, product3)).when(productService).getAll();
        doReturn(product1).when(productService).getById(1L);
        doReturn(product2).when(productService).getById(2L);
        doReturn(product3).when(productService).getById(3L);

        assertAll(
                () -> assertEquals(3, receiptService.getReceiptProducts("1-5 2-3 3-1").size()),
                () -> assertEquals(3, receiptService.getReceiptProducts("1-5 2-3 3-1 2-3").size())
        );
    }

    @Test
    void getReceiptProductsWithPromotionShouldReturnCorrectProductFullPrice() {
        List<ReceiptProduct> receiptProducts = List.of(
                new ReceiptProduct(product1, 5),
                new ReceiptProduct(product2, 3),
                new ReceiptProduct(product3, 1),
                new ReceiptProduct(product4, 1),
                new ReceiptProduct(product5, 1),
                new ReceiptProduct(product6, 2)
        );

        var actualList = receiptService.getReceiptProductsWithPromotion(receiptProducts);
        var actualProductFullPrice1 = actualList.get(0).getFullPrice();
        var actualProductFullPrice2 = actualList.get(1).getFullPrice();
        var actualProductFullPrice3 = actualList.get(2).getFullPrice();
        var actualProductFullPrice4 = actualList.get(3).getFullPrice();
        var actualProductFullPrice5 = actualList.get(4).getFullPrice();
        var actualProductFullPrice6 = actualList.get(5).getFullPrice();

        assertAll(
                () -> assertEquals(9.0, actualProductFullPrice1),
                () -> assertEquals(8.1, actualProductFullPrice2),
                () -> assertEquals(3.6, actualProductFullPrice3),
                () -> assertEquals(4.5, actualProductFullPrice4),
                () -> assertEquals(5.4, actualProductFullPrice5),
                () -> assertEquals(14.0, actualProductFullPrice6)
        );
    }

    @Test
    void checkGetFullPriceShouldReturnFullPrice() {
        List<ReceiptProduct> receiptProducts = List.of(
                new ReceiptProduct(product1, 5),
                new ReceiptProduct(product2, 3),
                new ReceiptProduct(product3, 1)
        );
        assertEquals(23.0, receiptService.getFullPrice(receiptProducts));
    }

    @Test
    void checkGetCardDiscountShouldReturnDiscount() {
        when(cardService.getAll()).thenReturn(List.of(card1, card2));
        when(cardService.getDiscountByNumber(1234)).thenReturn(10.0);

        var actualCardDiscount = receiptService.getCardDiscount("1-5 2-3 3-1 card-1234");
        assertAll(
                () -> assertEquals(10.0, actualCardDiscount),
                () -> assertNotEquals(20.0, actualCardDiscount)
        );
    }

    @Test
    void checkGetTotalPriceShouldReturnTotalPrice() {
        List<ReceiptProduct> receiptProducts = List.of(
                new ReceiptProduct(product1, 5),
                new ReceiptProduct(product2, 3),
                new ReceiptProduct(product3, 1)
        );

        when(cardService.getAll()).thenReturn(List.of(card1, card2));
        when(cardService.getDiscountByNumber(1234)).thenReturn(10.0);
        when(cardService.getDiscountByNumber(1111)).thenReturn(20.0);

        var actualTotalPrice1 = receiptService.getTotalPrice(receiptProducts, "1-5 2-3 3-1 card-1234");
        var actualTotalPrice2 = receiptService.getTotalPrice(receiptProducts, "1-5 2-3 3-1 card-1111");
        assertAll(
                () -> assertEquals(20.7, actualTotalPrice1),
                () -> assertEquals(18.4, actualTotalPrice2)
        );
    }

    @Test
    void getReceiptShouldReturnCorrectReceipt() {
        List<ReceiptProduct> receiptProducts = List.of(
                new ReceiptProduct(product1, 5),
                new ReceiptProduct(product2, 3),
                new ReceiptProduct(product3, 1),
                new ReceiptProduct(product4, 1),
                new ReceiptProduct(product5, 1),
                new ReceiptProduct(product6, 2)
        );

        doReturn(List.of(product1, product2, product3, product4, product5, product6)).when(productService).getAll();
        doReturn(product1).when(productService).getById(1L);
        doReturn(product2).when(productService).getById(2L);
        doReturn(product3).when(productService).getById(3L);
        doReturn(product4).when(productService).getById(4L);
        doReturn(product5).when(productService).getById(5L);
        doReturn(product6).when(productService).getById(6L);
        doReturn(List.of(card1)).when(cardService).getAll();
        doReturn(10.0).when(cardService).getDiscountByNumber(1234);

        var expectedReceipt = new Receipt(receiptProducts, 44.6, 10.0, 40.14);
        var actualReceipt = receiptService.getReceipt("1-5 2-3 3-1 4-1 5-1 6-2 card-1234");
        assertAll(
                () -> assertEquals(expectedReceipt.getReceiptProducts().size(), actualReceipt.getReceiptProducts().size()),
                () -> assertEquals(expectedReceipt.getFullPrice(), actualReceipt.getFullPrice()),
                () -> assertEquals(expectedReceipt.getCardDiscount(), actualReceipt.getCardDiscount()),
                () -> assertEquals(expectedReceipt.getTotalPrice(), actualReceipt.getTotalPrice())
        );
    }

    @Test
    void getReceiptLineListShouldReturnCorrectList() {
        doReturn(List.of(product1, product2, product3, product4, product5, product6)).when(productService).getAll();
        doReturn(product1).when(productService).getById(1L);
        doReturn(product2).when(productService).getById(2L);
        doReturn(product3).when(productService).getById(3L);
        doReturn(product4).when(productService).getById(4L);
        doReturn(product5).when(productService).getById(5L);
        doReturn(product6).when(productService).getById(6L);
        doReturn(List.of(card1)).when(cardService).getAll();
        doReturn(10.0).when(cardService).getDiscountByNumber(1234);

        assertEquals(RECEIPT_LINE_LIST, receiptService.getReceiptLineList("1-5 2-3 3-1 4-1 5-1 6-2 card-1234"));
    }
}
