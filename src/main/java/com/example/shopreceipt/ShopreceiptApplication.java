package com.example.shopreceipt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Shop Receipt - Application entry point
 */
@SpringBootApplication(scanBasePackages = "com.example.*")
public class ShopreceiptApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopreceiptApplication.class, args);
    }
}