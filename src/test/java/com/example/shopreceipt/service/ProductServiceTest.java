package com.example.shopreceipt.service;

import com.example.shopreceipt.entity.Product;
import com.example.shopreceipt.repository.ProductRepository;
import com.example.shopreceipt.service.util.ProductTestBuilder;
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

import static com.example.shopreceipt.service.constants.TestConstants.PRODUCT_NAME_1;
import static com.example.shopreceipt.service.constants.TestConstants.PRODUCT_NAME_2;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link ProductService}
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    private static Product product1 = new Product();
    private static Product product2 = new Product();
    private static Product product3 = new Product();

    @BeforeEach
    void setUp() {
        product1 = ProductTestBuilder.aProduct().withId(1L).withName(PRODUCT_NAME_1).withPrice(5.0).withPromotion(false).build();
        product2 = ProductTestBuilder.aProduct().withId(2L).withName(PRODUCT_NAME_2).build();
        product3 = ProductTestBuilder.aProduct().withId(3L).withPromotion(true).build();
    }

    @Nested
    class GetAllTest {
        @Test
        void checkGetAllShouldCalledRepositoryMethod() {
            productService.getAll();
            verify(productRepository, times(1)).findAll();
        }

        @Test
        void checkGetAllShouldReturnAllProducts() {
            when(productRepository.findAll()).thenReturn(List.of(product1, product2));
            assertAll(
                    () -> assertEquals(List.of(product1, product2), productService.getAll()),
                    () -> assertEquals(2, productService.getAll().size()),
                    () -> assertNotEquals(List.of(product1, product2, product3), productService.getAll()),
                    () -> assertNotEquals(List.of(product2, product3), productService.getAll())
            );
        }

        @Test
        void checkGetAllShouldReturnEmptyList() {
            when(productRepository.findAll()).thenReturn(Collections.emptyList());
            assertEquals(Collections.emptyList(), productService.getAll());
        }
    }

    @Nested
    class GetByIdTest {
        @Test
        void checkGetByIdShouldReturnProduct() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
            when(productRepository.findById(3L)).thenReturn(Optional.of(product3));
            assertAll(
                    () -> assertEquals(product1, productService.getById(1L)),
                    () -> assertNotEquals(product3, productService.getById(1L)),
                    () -> assertNotEquals(product1, productService.getById(3L))
            );
        }

        @Test
        void checkGetByIdShouldThrowEntityNotFoundException() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
            assertAll(
                    () -> assertDoesNotThrow(() -> productService.getById(1L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> productService.getById(0L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> productService.getById(null))
            );
        }

        @Test
        void checkGetByIdShouldThrowNullPointerException() {
            when(productRepository.findById(1L)).thenReturn(null);
            assertThrows(NullPointerException.class, () -> productService.getById(1L));
        }
    }

    @Test
    void checkSaveShouldCalledRepositoryMethod() {
        productService.save(product1);
        verify(productRepository, times(1)).save(product1);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void checkDeleteByIdShouldCalledRepositoryMethod(long argument) {
        productService.deleteById(argument);
        verify(productRepository, times(1)).deleteById(argument);
    }

    @Nested
    class GetProductNameTest {
        @Test
        void checkGetProductNameShouldReturnName() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
            when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
            assertAll(
                    () -> assertEquals(PRODUCT_NAME_1, productService.getProductName(1L)),
                    () -> assertNotEquals(PRODUCT_NAME_1, productService.getProductName(2L))
            );
        }

        @Test
        void checkGetProductNameShouldThrowEntityNotFoundException() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
            assertAll(
                    () -> assertDoesNotThrow(() -> productService.getProductName(1L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> productService.getProductName(2L))
            );
        }
    }

    @Nested
    class GetProductPriceTest {
        @Test
        void checkGetProductPriceShouldReturnPrice() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
            assertEquals(5.0, productService.getProductPrice(1L));
        }

        @Test
        void checkGetProductPriceShouldThrowEntityNotFoundException() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
            assertAll(
                    () -> assertDoesNotThrow(() -> productService.getProductPrice(1L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> productService.getProductPrice(2L))
            );
        }
    }

    @Nested
    class GetProductPromotionTest {
        @Test
        void checkGetProductPromotionShouldReturnPromotion() {
            when(productRepository.findById(3L)).thenReturn(Optional.of(product3));
            assertTrue(productService.getProductPromotion(3L));
        }

        @Test
        void checkGetProductPromotionShouldThrowEntityNotFoundException() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
            assertAll(
                    () -> assertDoesNotThrow(() -> productService.getProductPromotion(1L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> productService.getProductPromotion(2L))
            );
        }
    }
}