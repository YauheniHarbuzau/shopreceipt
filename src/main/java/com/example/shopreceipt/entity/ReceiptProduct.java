package com.example.shopreceipt.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiptProduct {

    private String name;
    private Double price;
    private Boolean promotion;
    private Integer amount;
    private Double fullPrice;

    public ReceiptProduct(Product product, Integer amount) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.promotion = product.getPromotion();
        this.amount = amount;
        this.fullPrice = this.price * amount;
    }
}
