package com.example.shopreceipt.constants;

import java.util.List;

/**
 * Constants
 */
public class Constants {

    public static final String INSERT_PRODUCT = "INSERT INTO product (name, price, promotion) VALUES (?, ?, ?)";
    public static final String INSERT_CARD = "INSERT INTO card (number, discount) VALUES (?, ?)";

    public static final String RECEIPT_HEAD = "|****************RECEIPT****************|";
    public static final String RECEIPT_LINE_FORMAT_1 = "| %-12s | %6s | %4s | %6s |";
    public static final String RECEIPT_LINE_FORMAT_2 = "| %-28s | %6s |";
    public static final String RECEIPT_LINE_SEPARATION = "|***************************************|";

    public static final String FILE_PATH = "";
    public static final String PDF_RECEIPT_FILE = "receipt.pdf";
    public static final String XML_PRODUCTS_FILE = "products-response-info.xml";
    public static final String XML_CARDS_FILE = "cards-response-info.xml";

    public static final List<String> RECEIPT_LINE_LIST = List.of(
            RECEIPT_HEAD,
            RECEIPT_LINE_SEPARATION,
            "| product1     |  2,00$ |   x5 |  9,00$ |",
            "| product2     |  3,00$ |   x3 |  8,10$ |",
            "| product3     |  4,00$ |   x1 |  3,60$ |",
            "| product4     |  5,00$ |   x1 |  4,50$ |",
            "| product5     |  6,00$ |   x1 |  5,40$ |",
            "| product6     |  7,00$ |   x2 | 14,00$ |",
            RECEIPT_LINE_SEPARATION,
            "| full price:                  | 44,60$ |",
            "| discount:                    | 10,00% |",
            "| total price:                 | 40,14$ |",
            RECEIPT_LINE_SEPARATION
    );
}
