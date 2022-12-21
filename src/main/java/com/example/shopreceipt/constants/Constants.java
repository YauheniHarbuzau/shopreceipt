package com.example.shopreceipt.constants;

/**
 * Constants
 */
public class Constants {

    public static final String INSERT_PRODUCT = "INSERT INTO product (name, price, promotion) VALUES (?, ?, ?)";
    public static final String INSERT_CARD = "INSERT INTO card (number, discount) VALUES (?, ?)";

    public static final String LINE_FORMAT = "%-15s %15s %n";
    public static final String LINE_SEPARATION = "******************************";

    public static final String FILE_PATH = "D:/";
    public static final String FILE_NAME = "receipt.txt";
}