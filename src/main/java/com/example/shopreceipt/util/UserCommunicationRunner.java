package com.example.shopreceipt.util;

import com.example.shopreceipt.service.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static com.example.shopreceipt.constants.Constants.FILE_NAME;
import static com.example.shopreceipt.constants.Constants.FILE_PATH;

/**
 * Class for receiving data from the user and generating a receipt
 */
@AllArgsConstructor
@Component
public class UserCommunicationRunner implements CommandLineRunner {

    private final ReceiptService receiptService;
    private final InsertToDataBase insertToDataBasel;

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
        System.out.println("Введите данные");
        return in.nextLine();
    }

    /**
     * Вывод кассового чека в консоль и в файл
     *
     * @param line переданная пользователем строка с набором параметров (String)
     */
    private void getReceipt(String line) throws IOException {
        var list = receiptService.generateFullReceipt(line);
        FileWriter fileWriter = new FileWriter(FILE_PATH + FILE_NAME);
        for (String str : list) {
            System.out.println(str.trim());
            fileWriter.write(str.trim() + System.lineSeparator());
        }
        fileWriter.close();
    }
}
