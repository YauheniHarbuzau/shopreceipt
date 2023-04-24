package com.example.shopreceipt.service;

import com.example.shopreceipt.entity.Receipt;
import com.example.shopreceipt.entity.ReceiptProduct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shopreceipt.constants.Constants.RECEIPT_HEAD;
import static com.example.shopreceipt.constants.Constants.RECEIPT_LINE_FORMAT_1;
import static com.example.shopreceipt.constants.Constants.RECEIPT_LINE_FORMAT_2;
import static com.example.shopreceipt.constants.Constants.RECEIPT_LINE_SEPARATION;

/**
 * Service for the Receipt
 */
@AllArgsConstructor
@Service
public class ReceiptService {

    private final ProductService productService;
    private final CardService cardService;

    /**
     * Проверка соответствия шаблону
     *
     * @param source строка с набором параметров (String)
     * @return булевое значение соответствует(true)/не соответствует(false)
     */
    public boolean isValid(String source) {
        return source.matches("^(\\d+-\\d+ )*\\d+-\\d+$") || source.matches("^(\\d+-\\d+ )*card-\\d+$");
    }

    /**
     * Проверка есть ли товар в базе данных
     *
     * @param id товара (long)
     * @return булевое значение да(true)/нет(false)
     */
    public boolean productIsPresent(long id) {
        return productService.getAll().stream().anyMatch(product -> product.getId() == id);
    }

    /**
     * Проверка есть ли платежная карта в базе данных
     *
     * @param number номер платежной карты (int)
     * @return булевое значение да(true)/нет(false)
     */
    public boolean cardIsPresent(int number) {
        return cardService.getAll().stream().anyMatch(card -> card.getNumber() == number);
    }

    /**
     * Получение из строки Map с ID товара и его количеством
     *
     * @param source строка с набором параметров (String)
     * @return Map<Long, Integer>
     */
    public Map<Long, Integer> sourceToMap(String source) {
        String[] array = source.split(" ");
        Map<Long, Integer> map = new HashMap<>(array.length);
        Map<Long, Integer> newMap = new HashMap<>();
        for (String str : array) {
            if (str.matches("^\\d+-\\d+$")) {
                map.put(
                        Long.parseLong(str.substring(0, str.indexOf('-'))),
                        Integer.parseInt(str.substring(str.indexOf('-') + 1))
                );
            }
        }
        for (Map.Entry<Long, Integer> item : map.entrySet()) {
            if (productIsPresent(item.getKey())) {
                newMap.put(item.getKey(), item.getValue());
            }
        }
        return newMap;
    }

    /**
     * Получение списка товаров в кассовом чеке (наименование, цена, акция и т.д.)
     *
     * @param source строка с набором параметров (String)
     * @return список товаров в кассовом чеке
     */
    public List<ReceiptProduct> getReceiptProducts(String source) {
        var longIntMap = sourceToMap(source);
        List<ReceiptProduct> receiptProducts = new ArrayList<>(longIntMap.size());
        for (Map.Entry<Long, Integer> item : longIntMap.entrySet()) {
            receiptProducts.add(new ReceiptProduct(productService.getById(item.getKey()), item.getValue()));
        }
        return getReceiptProductsWithPromotion(receiptProducts);
    }

    /**
     * Корректировка списка товаров с учетом акционных товаров
     * (при наличии 5 позиций акционных товаров на них будет выдана скидка 10%)
     *
     * @param receiptProducts список товаров в кассовом чеке
     * @return список товаров в кассовом чеке с учетом скидки по акции
     */
    public List<ReceiptProduct> getReceiptProductsWithPromotion(List<ReceiptProduct> receiptProducts) {
        var count = receiptProducts.stream().filter(ReceiptProduct::getPromotion).count();
        if (count >= 5) {
            for (ReceiptProduct receiptProduct : receiptProducts) {
                if (receiptProduct.getPromotion()) {
                    var fullPrice = receiptProduct.getFullPrice();
                    receiptProduct.setFullPrice(fullPrice * 0.9);
                }
            }
        }
        return receiptProducts;
    }

    /**
     * Получение полной цены кассового чека
     *
     * @param receiptProducts список товаров в кассовом чеке
     * @return полная цена (double)
     */
    public double getFullPrice(List<ReceiptProduct> receiptProducts) {
        return receiptProducts.stream().mapToDouble(ReceiptProduct::getFullPrice).sum();
    }

    /**
     * Получение величины скидки платежной карты
     *
     * @param source строка с набором параметров (String)
     * @return процент скидки (double)
     */
    public double getCardDiscount(String source) {
        if (source.contains("card")) {
            var number = Integer.parseInt(source.substring(source.lastIndexOf('-') + 1));
            return cardIsPresent(number) ? cardService.getDiscountByNumber(number) : 0.0;
        } else {
            return 0.0;
        }
    }

    /**
     * Получение итоговой цены кассового чека с учетом скидки платежной карты
     *
     * @param receiptProducts список товаров в кассовом чеке
     * @param source          строка с набором параметров (String)
     * @return итоговая цена (double)
     */
    public double getTotalPrice(List<ReceiptProduct> receiptProducts, String source) {
        var fullPrice = getFullPrice(receiptProducts);
        var cardDiscount = getCardDiscount(source);
        return cardDiscount != 0 ? fullPrice - (fullPrice / 100 * cardDiscount) : fullPrice;
    }

    /**
     * Формирование кассового чека
     *
     * @param source строка с набором параметров (String)
     * @return кассовый чек
     */
    public Receipt getReceipt(String source) {
        var receiptProducts = getReceiptProducts(source);
        var fullPrice = getFullPrice(receiptProducts);
        var cardDiscount = getCardDiscount(source);
        var totalPrice = getTotalPrice(receiptProducts, source);
        return new Receipt(receiptProducts, fullPrice, cardDiscount, totalPrice);
    }

    /**
     * Вывод кассового чека в консоль
     *
     * @param source строка с набором параметров (String)
     */
    public List<String> getReceiptLineList(String source) {
        DecimalFormat dF = new DecimalFormat("0.00");
        var receipt = getReceipt(source);
        var receiptProducts = receipt.getReceiptProducts();

        List<String> receiptLineList = new ArrayList<>(receiptProducts.size() + 7);
        receiptLineList.add(RECEIPT_HEAD);
        receiptLineList.add(RECEIPT_LINE_SEPARATION);
        for (ReceiptProduct receiptProduct : receiptProducts) {
            receiptLineList.add(String.format(
                    RECEIPT_LINE_FORMAT_1,
                    receiptProduct.getName(),
                    dF.format(receiptProduct.getPrice()) + "$",
                    "x" + receiptProduct.getAmount(),
                    dF.format(receiptProduct.getFullPrice()) + "$"
            ));
        }
        receiptLineList.add(RECEIPT_LINE_SEPARATION);
        receiptLineList.add(String.format(RECEIPT_LINE_FORMAT_2, "full price:", dF.format(receipt.getFullPrice()) + "$"));
        receiptLineList.add(String.format(RECEIPT_LINE_FORMAT_2, "discount:", dF.format(receipt.getCardDiscount()) + "%"));
        receiptLineList.add(String.format(RECEIPT_LINE_FORMAT_2, "total price:", dF.format(receipt.getTotalPrice()) + "$"));
        receiptLineList.add(RECEIPT_LINE_SEPARATION);
        return receiptLineList;
    }
}
