package com.example.shopreceipt.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Constants
 */
public class Constants {

    public static final String INSERT_PRODUCT = "INSERT INTO product (name, price, promotion) VALUES (?, ?, ?)";
    public static final String INSERT_CARD = "INSERT INTO card (number, discount) VALUES (?, ?)";

    public static final String LINE_FORMAT = "%-15s %15s";
    public static final String LINE_SEPARATION = "******************************";

    public static final String FILE_PATH = "D:/";
    public static final String FILE_NAME = "receipt.txt";

    public static final String XML_PRODUCTS = "products-response-info.xml";
    public static final String XML_CARDS = "cards-response-info.xml";

    public static final List<String> RECEIPT_LIST_1 = new ArrayList<>(10);
    public static final List<String> RECEIPT_LIST_2 = new ArrayList<>(10);

    static {
        Collections.addAll(
                RECEIPT_LIST_1,
                "******************************",
                "product 3  x1            15,00$",
                "product 1  x5            25,00$",
                "product 2  x3            30,00$",
                "******************************",
                "сумма                    70,00$",
                "скидка                    0,00%",
                "******************************",
                "итого к оплате           70,00$",
                "******************************"
        );
        Collections.addAll(
                RECEIPT_LIST_2,
                "******************************",
                "product 3  x1            15,00$",
                "product 1  x5            25,00$",
                "product 2  x3            30,00$",
                "******************************",
                "сумма                    70,00$",
                "скидка                   10,00%",
                "******************************",
                "итого к оплате           63,00$",
                "******************************"
        );
    }
}
