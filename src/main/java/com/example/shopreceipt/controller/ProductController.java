package com.example.shopreceipt.controller;

import com.example.shopreceipt.entity.Product;
import com.example.shopreceipt.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for the {@link Product}
 *
 * @see ProductService
 */
@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/example")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/products/{id}")
    public Product getById(@PathVariable("id") @NotNull Long id) {
        return productService.getById(id);
    }

    @PostMapping("/products")
    public void save(@RequestBody @Valid Product product) {
        productService.save(product);
    }

    @DeleteMapping("/products/{id}")
    public void deleteById(@PathVariable("id") @NotNull Long id) {
        productService.deleteById(id);
    }
}