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

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
        product1 = ProductTestBuilder.aProduct().withId(1L).withName("product 1").withPrice(5.0).withPromotion(false).build();
        product2 = ProductTestBuilder.aProduct().withId(2L).withName("product 2").build();
        product3 = ProductTestBuilder.aProduct().withId(3L).withPromotion(true).build();
    }

    @Nested
    class GetAllTest {
        @Test
        void checkGetAllShouldCalledRepositoryMethod() {
            productService.getAll();
            verify(productRepository).findAll();
        }

        @Test
        void checkGetAllShouldReturnAllProducts() {
            doReturn(List.of(product1, product2)).when(productRepository).findAll();
            assertAll(
                    () -> assertEquals(List.of(product1, product2), productService.getAll()),
                    () -> assertEquals(2, productService.getAll().size()),
                    () -> assertNotEquals(List.of(product1, product2, product3), productService.getAll()),
                    () -> assertNotEquals(List.of(product2, product3), productService.getAll())
            );
        }

        @Test
        void checkGetAllShouldReturnEmptyList() {
            doReturn(emptyList()).when(productRepository).findAll();
            assertEquals(emptyList(), productService.getAll());
        }
    }

    @Nested
    class GetByIdTest {
        @Test
        void checkGetByIdShouldReturnProduct() {
            doReturn(Optional.of(product1)).when(productRepository).findById(1L);
            doReturn(Optional.of(product3)).when(productRepository).findById(3L);
            assertAll(
                    () -> assertEquals(product1, productService.getById(1L)),
                    () -> assertNotEquals(product3, productService.getById(1L)),
                    () -> assertNotEquals(product1, productService.getById(3L))
            );
        }

        @Test
        void checkGetByIdShouldThrowEntityNotFoundException() {
            doReturn(Optional.of(product1)).when(productRepository).findById(1L);
            assertAll(
                    () -> assertDoesNotThrow(() -> productService.getById(1L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> productService.getById(0L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> productService.getById(null))
            );
        }

        @Test
        void checkGetByIdShouldThrowNullPointerException() {
            doReturn(null).when(productRepository).findById(1L);
            assertThrows(NullPointerException.class, () -> productService.getById(1L));
        }
    }

    @Test
    void checkSaveShouldCalledRepositoryMethod() {
        productService.save(product1);
        verify(productRepository).save(product1);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void checkDeleteByIdShouldCalledRepositoryMethod(long argument) {
        productService.deleteById(argument);
        verify(productRepository).deleteById(argument);
    }

    @Nested
    class GetProductNameTest {
        @Test
        void checkGetProductNameShouldReturnName() {
            doReturn(Optional.of(product1)).when(productRepository).findById(1L);
            doReturn(Optional.of(product2)).when(productRepository).findById(2L);
            assertAll(
                    () -> assertEquals("product 1", productService.getProductName(1L)),
                    () -> assertNotEquals("product 1", productService.getProductName(2L))
            );
        }

        @Test
        void checkGetProductNameShouldThrowEntityNotFoundException() {
            doReturn(Optional.of(product1)).when(productRepository).findById(1L);
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
            doReturn(Optional.of(product1)).when(productRepository).findById(1L);
            assertEquals(5.0, productService.getProductPrice(1L));
        }

        @Test
        void checkGetProductPriceShouldThrowEntityNotFoundException() {
            doReturn(Optional.of(product1)).when(productRepository).findById(1L);
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
            doReturn(Optional.of(product3)).when(productRepository).findById(3L);
            assertTrue(productService.getProductPromotion(3L));
        }

        @Test
        void checkGetProductPromotionShouldThrowEntityNotFoundException() {
            doReturn(Optional.of(product1)).when(productRepository).findById(1L);
            assertAll(
                    () -> assertDoesNotThrow(() -> productService.getProductPromotion(1L)),
                    () -> assertThrows(EntityNotFoundException.class, () -> productService.getProductPromotion(2L))
            );
        }
    }
}
