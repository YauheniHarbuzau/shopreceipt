package com.example.shopreceipt;

import com.example.shopreceipt.entity.Product;
import com.example.shopreceipt.repository.ProductRepository;
import com.example.shopreceipt.service.ProductService;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link ProductService}
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private static final Product product1 = new Product();
    private static final Product product2 = new Product();
    private static final Product product3 = new Product();

    @BeforeEach
    public void setup() {
        product1.setId(1L);
        product1.setName("product 1");
        product1.setPrice(5.0);
        product1.setPromotion(false);
        product2.setId(2L);
        product3.setId(3L);
    }

    @Test
    @DisplayName("getAll test")
    public void getAll() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        assertEquals(List.of(product1, product2), productService.getAll());
        assertNotEquals(List.of(product1, product2, product3), productService.getAll());
        assertNotEquals(List.of(product2, product3), productService.getAll());
    }

    @Test
    @DisplayName("getById test")
    public void getById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(productRepository.findById(3L)).thenReturn(Optional.of(product3));
        assertEquals(product1, productService.getById(1L));
        assertEquals(product2, productService.getById(2L));
        assertEquals(product3, productService.getById(3L));
        assertNotEquals(product2, productService.getById(1L));
        assertNotEquals(product3, productService.getById(1L));
    }

    @Test
    @DisplayName("save test")
    public void save() {
        productService.save(product1);
        verify(productRepository).save(product1);
    }

    @Test
    @DisplayName("deleteById test")
    public void deleteById() {
        productService.deleteById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    @DisplayName("getProductName test")
    public void getProductName() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        assertEquals("product 1", productService.getProductName(1L));
    }

    @Test
    @DisplayName("getProductPrice test")
    public void getProductPrice() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        assertEquals(5.0, productService.getProductPrice(1L));
    }

    @Test
    @DisplayName("getProductPromotion test")
    public void getProductPromotion() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        assertFalse(productService.getProductPromotion(1L));
    }
}