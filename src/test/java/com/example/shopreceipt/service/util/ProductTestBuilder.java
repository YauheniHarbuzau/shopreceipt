package com.example.shopreceipt.service.util;

import com.example.shopreceipt.entity.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aProduct")
@With
public class ProductTestBuilder implements TestBuilder<Product> {

    private Long id = 0L;
    private String name = "";
    private Double price = 0.0;
    private Boolean promotion = false;

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
