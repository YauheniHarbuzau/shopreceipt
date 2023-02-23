package com.example.shopreceipt.service.util;

import com.example.shopreceipt.entity.Product;

public class ProductTestBuilder implements TestBuilder<Product> {

    private Long id = 0L;
    private String name = "";
    private Double price = 0.0;
    private Boolean promotion = false;

    private ProductTestBuilder() {
    }

    public static ProductTestBuilder aProduct() {
        return new ProductTestBuilder();
    }

    public ProductTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductTestBuilder withPrice(Double price) {
        this.price = price;
        return this;
    }

    public ProductTestBuilder withPromotion(Boolean promotion) {
        this.promotion = promotion;
        return this;
    }

    @Override
    public Product build() {
        final var product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setPromotion(promotion);
        return product;
    }
}