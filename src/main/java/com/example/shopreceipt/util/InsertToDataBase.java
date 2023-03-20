package com.example.shopreceipt.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.shopreceipt.constants.Constants.INSERT_CARD;
import static com.example.shopreceipt.constants.Constants.INSERT_PRODUCT;

/**
 * Class for database autocomplete
 */
@Component
@Configuration
public class InsertToDataBase {

    private Connection connection;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    public void insertData() {
        saveProduct("bread", 2.0, false);
        saveProduct("long loaf", 2.5, true);
        saveProduct("flour", 1.5, false);
        saveProduct("eggs", 2.5, true);
        saveProduct("chicken", 4.5, true);
        saveProduct("pork", 6.5, false);
        saveProduct("beef", 7.5, false);
        saveProduct("pollock", 6.0, false);
        saveProduct("flounder", 8.0, true);
        saveProduct("water", 1.0, false);
        saveProduct("soda", 1.5, false);
        saveProduct("juice", 1.5, true);

        saveCard(1234, 5.0);
        saveCard(1111, 10.0);
        saveCard(3333, 15.0);
    }

    private void getConnection() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void saveProduct(String name, Double price, Boolean promotion) {
        getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(INSERT_PRODUCT);
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setBoolean(3, promotion);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void saveCard(Integer number, Double discount) {
        getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(INSERT_CARD);
            ps.setInt(1, number);
            ps.setDouble(2, discount);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
