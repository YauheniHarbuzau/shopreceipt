package com.example.shopreceipt.util;

import com.example.shopreceipt.service.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

/**
 * Class for receiving data from the user and generating a receipt
 */
@AllArgsConstructor
@Component
public class UserCommunicationRunner implements CommandLineRunner {

    private final ReceiptService receiptService;
    private final InsertToDataBase insertToDataBasel;
    private final PdfCreating pdfCreating;

    @Override
    public void run(String... args) throws Exception {
        insertToDataBasel.insertData();
        getReceipt(userInput().trim());
    }

    /**
     * Получение данных от пользователя
     *
     * @return строка с набором параметров (String)
     */
    private String userInput() {
        Scanner in = new Scanner(System.in);
        System.out.println("Make an input:");
        return in.nextLine();
    }

    /**
     * Вывод кассового чека в консоль и в файл pdf
     *
     * @param source переданная пользователем строка с набором параметров (String)
     */
    private void getReceipt(String source) throws IOException {
        pdfCreating.addPdf(source);
        receiptService.getReceiptLineList(source).forEach(System.out::println);
    }
}
