package com.example.shopreceipt.service.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: перенести константы в main в класс Constants
public class TestConstants {

    public static final String PRODUCT_NAME_1 = "product 1";
    public static final String PRODUCT_NAME_2 = "product 2";
    public static final String PRODUCT_NAME_3 = "product 3";

    public static final Map<Long, Integer> MAP_LONG_INT = new HashMap<>(3);
    public static final Map<String, Double> PRICE_MAP = new HashMap<>(3);
    public static final Map<String, Integer> AMOUNT_MAP = new HashMap<>(3);

    public static final String SOURCE_LINE_1 = "1-5 2-3 3-1";
    public static final String SOURCE_LINE_2 = "1-5 2-3 3-1 card-1234";
    public static final String SOURCE_LINE_3 = "1-7 2-3 3-1";

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