package com.example.shopreceipt;

import com.example.shopreceipt.entity.Card;
import com.example.shopreceipt.entity.Product;
import com.example.shopreceipt.service.CardService;
import com.example.shopreceipt.service.ProductService;
import com.example.shopreceipt.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link ReceiptService}
 */
@ExtendWith(MockitoExtension.class)
public class ReceiptServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private CardService cardService;

    @InjectMocks
    private ReceiptService receiptService;

    private static final Map<Long, Integer> mapLongInt = new HashMap<>(3);
    private static final Map<String, Double> priceMap = new HashMap<>(3);
    private static final Map<String, Integer> amountMap = new HashMap<>(3);
    private static final Product product1 = new Product();
    private static final Product product2 = new Product();
    private static final Product product3 = new Product();
    private static final Card card1 = new Card();
    private static final Card card2 = new Card();

    @BeforeEach
    public void setup() {

        product1.setId(1L);
        product1.setName("product 1");
        product1.setPrice(5.0);
        product1.setPromotion(false);
        product2.setId(2L);
        product2.setName("product 2");
        product2.setPrice(10.0);
        product2.setPromotion(true);
        product3.setId(3L);
        product3.setName("product 3");
        product3.setPrice(15.0);
        product3.setPromotion(true);

        card1.setId(1L);
        card1.setNumber(1234);
        card1.setDiscount(10.0);
        card2.setId(2L);
        card2.setNumber(1111);
        card2.setDiscount(20.0);

        mapLongInt.put(1L, 5);
        mapLongInt.put(2L, 3);
        mapLongInt.put(3L, 1);

        priceMap.put("product 1", 5.0);
        priceMap.put("product 2", 10.0);
        priceMap.put("product 3", 15.0);

        amountMap.put("product 1", 5);
        amountMap.put("product 2", 3);
        amountMap.put("product 3", 1);
    }

    @Test
    @DisplayName("isValid test")
    public void isValid() {
        assertTrue(receiptService.isValid("1-3 2-1 card-1"));
        assertTrue(receiptService.isValid("1-3 2-1 3-1"));
        assertFalse(receiptService.isValid("is not valid"));
        assertFalse(receiptService.isValid("1-32-1 3-1"));
    }

    @Test
    @DisplayName("productIsPresent test")
    public void productIsPresent() {
        when(productService.getAll()).thenReturn(List.of(product1, product2));
        assertTrue(receiptService.productIsPresent(1L));
        assertTrue(receiptService.productIsPresent(2L));
        assertFalse(receiptService.productIsPresent(3L));
    }

    @Test
    @DisplayName("sourceToMap test")
    public void sourceToMap() {
        when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
        assertEquals(mapLongInt, receiptService.sourceToMap("1-5 2-3 3-1"));
        assertEquals(mapLongInt, receiptService.sourceToMap("1-5 3-1 2-3 3-1 card-1234"));
        assertNotEquals(mapLongInt, receiptService.sourceToMap("1-7 2-3 3-1"));
        assertNotEquals(mapLongInt, receiptService.sourceToMap("1-7 3-1 2-3 3-1 card-1234"));
    }

    @Test
    @DisplayName("getPriceMap test")
    public void getPriceMap() {
        when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
        when(productService.getProductName(1L)).thenReturn(product1.getName());
        when(productService.getProductName(2L)).thenReturn(product2.getName());
        when(productService.getProductName(3L)).thenReturn(product3.getName());
        when(productService.getProductPrice(1L)).thenReturn(product1.getPrice());
        when(productService.getProductPrice(2L)).thenReturn(product2.getPrice());
        when(productService.getProductPrice(3L)).thenReturn(product3.getPrice());
        assertEquals(priceMap, receiptService.getPriceMap("1-1 2-1 3-1"));
        assertEquals(priceMap, receiptService.getPriceMap("1-1 2-1 3-1 card-1234"));
        assertNotEquals(priceMap, receiptService.getPriceMap("1-1 2-1 card-1234"));
    }

    @Test
    @DisplayName("getAmountMap test")
    public void getAmountMap() {
        when(productService.getAll()).thenReturn(List.of(product1, product2, product3));
        when(productService.getProductName(1L)).thenReturn(product1.getName());
        when(productService.getProductName(2L)).thenReturn(product2.getName());
        when(productService.getProductName(3L)).thenReturn(product3.getName());
        assertEquals(amountMap, receiptService.getAmountMap("1-5 2-3 3-1"));
        assertNotEquals(amountMap, receiptService.getAmountMap("1-7 2-3 3-1"));
    }

    @Test
    @DisplayName("countPromotionProduct test")
    public void countPromotionProduct() {
        when(productService.getProductPromotion(1L)).thenReturn(product1.getPromotion());
        when(productService.getProductPromotion(2L)).thenReturn(product2.getPromotion());
        when(productService.getProductPromotion(3L)).thenReturn(product3.getPromotion());
        assertEquals(2, receiptService.countPromotionProduct(mapLongInt));
        assertNotEquals(3, receiptService.countPromotionProduct(mapLongInt));
    }

    @Test
    @DisplayName("getFullPrice test")
    public void getFullPrice() {
        assertEquals(30.0, receiptService.getFullPrice(priceMap));
        assertNotEquals(31.0, receiptService.getFullPrice(priceMap));
    }

    @Test
    @DisplayName("cardIsPresent test")
    public void cardIsPresent() {
        when(cardService.getAll()).thenReturn(List.of(card1, card2));
        assertTrue(receiptService.cardIsPresent(1234));
        assertTrue(receiptService.cardIsPresent(1111));
        assertFalse(receiptService.cardIsPresent(3333));
    }

    @Test
    @DisplayName("getCardDiscount1 test")
    public void getCardDiscount1() {
        String source = "1-5 2-3 3-1 card-1234";
        when(cardService.getAll()).thenReturn(List.of(card1, card2));
        when(cardService.getDiscountByNumber(1234)).thenReturn(10.0);
        assertEquals(10.0, receiptService.getCardDiscount(source));
        assertNotEquals(15.0, receiptService.getCardDiscount(source));
    }

    @Test
    @DisplayName("getCardDiscount2 test")
    public void getCardDiscount2() {
        String source = "1-5 2-3 3-1";
        assertEquals(0.0, receiptService.getCardDiscount(source));
        assertNotEquals(10.0, receiptService.getCardDiscount(source));
    }

    @Test
    @DisplayName("getTotalPrice1 test")
    public void getTotalPrice1() {
        String source = "1-5 2-3 3-1 card-1234";
        when(cardService.getAll()).thenReturn(List.of(card1, card2));
        when(cardService.getDiscountByNumber(1234)).thenReturn(10.0);
        assertEquals(27.0, receiptService.getTotalPrice(priceMap, source));
        assertNotEquals(27.1, receiptService.getTotalPrice(priceMap, source));
    }

    @Test
    @DisplayName("getTotalPrice2 test")
    public void getTotalPrice2() {
        String source = "1-5 2-3 3-1";
        assertEquals(30.0, receiptService.getTotalPrice(priceMap, source));
        assertNotEquals(31.0, receiptService.getTotalPrice(priceMap, source));
    }
}