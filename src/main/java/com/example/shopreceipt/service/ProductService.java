package com.example.shopreceipt.service;

import com.example.shopreceipt.entity.Product;
import com.example.shopreceipt.exeption.EntityIsNotCorrectException;
import com.example.shopreceipt.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for the {@link Product}
 *
 * @see ProductRepository
 */
@AllArgsConstructor
@Service
public class ProductService implements AbstractService<Product> {

    private final ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product not found by id: " + id)
        );
    }

    public void save(Product product) {
        try {
            productRepository.save(product);
        } catch (RuntimeException ex) {
            throw new EntityIsNotCorrectException("Product entity is not correct");
        }
    }

    public void deleteById(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException("Product not found by id: " + id);
        }
    }

    public String getProductName(Long id) {
        return getById(id).getName();
    }

    public Double getProductPrice(Long id) {
        return getById(id).getPrice();
    }

    public boolean getProductPromotion(Long id) {
        return getById(id).getPromotion();
    }
}