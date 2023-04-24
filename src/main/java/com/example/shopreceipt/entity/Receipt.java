package com.example.shopreceipt.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Receipt {

    private List<ReceiptProduct> receiptProducts;
    private Double fullPrice;
    private Double cardDiscount;
    private Double totalPrice;
}
