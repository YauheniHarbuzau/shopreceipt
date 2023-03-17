package com.example.shopreceipt.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shopreceipt.constants.Constants.LINE_FORMAT;
import static com.example.shopreceipt.constants.Constants.LINE_SEPARATION;

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
        return source.matches("((([0-9]*)([-])([0-9]*)[ ])*)([0-9]*)([-])([0-9]*)") ||
                source.matches("((([0-9]*)([-])([0-9]*)[ ])*)(card-)([0-9]*)");
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
     * Получение из строки Map с ID товара и его количеством
     *
     * @param source строка с набором параметров (String)
     * @return Map<Long, Integer>
     */
    public Map<Long, Integer> sourceToMap(String source) {
        String[] array = source.split("[ ]");
        Map<Long, Integer> map = new HashMap<>(array.length);
        Map<Long, Integer> newMap = new HashMap<>();
        for (String str : array) {
            if (str.matches("(([0-9]*)([-])([0-9]*)*)")) {
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
     * Получение из строки Map с наименованием товара и его ценой с учетом количества
     *
     * @param source строка с набором параметров (String)
     * @return Map<String, Double>
     */
    public Map<String, Double> getPriceMap(String source) {
        Map<String, Double> priceMap = new HashMap<>();
        if (isValid(source)) {
            Map<Long, Integer> map = sourceToMap(source);
            for (Map.Entry<Long, Integer> item : map.entrySet()) {
                if (countPromotionProduct(map) >= 5) {
                    priceMap.put(
                            productService.getProductName(item.getKey()),
                            (productService.getProductPrice(item.getKey()) * item.getValue()) * 0.9
                    );
                } else {
                    priceMap.put(
                            productService.getProductName(item.getKey()),
                            productService.getProductPrice(item.getKey()) * item.getValue()
                    );
                }
            }
        }
        return priceMap;
    }

    /**
     * Получение из строки Map с наименованием товара и его количеством
     *
     * @param source строка с набором параметров (String)
     * @return Map<String, Integer>
     */
    public Map<String, Integer> getAmountMap(String source) {
        Map<String, Integer> amountMap = new HashMap<>();
        if (isValid(source)) {
            Map<Long, Integer> map = sourceToMap(source);
            for (Map.Entry<Long, Integer> item : map.entrySet()) {
                amountMap.put(
                        productService.getProductName(item.getKey()),
                        item.getValue()
                );
            }
        }
        return amountMap;
    }

    /**
     * Получение количества акционных товаров
     *
     * @param map с ID товара и его количеством
     * @return количество акционных товаров (int)
     */
    public int countPromotionProduct(Map<Long, Integer> map) {
        int count = 0;
        for (Long id : map.keySet()) {
            if (productService.getProductPromotion(id)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Получение полной цены кассового чека
     *
     * @param map с наименованием товара и его ценой с учетом количества
     * @return полная цена (double)
     */
    public double getFullPrice(Map<String, Double> map) {
        return map.values().stream().mapToDouble(Double::doubleValue).sum();
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
     * @param map    с наименованием товара и его ценой с учетом количества
     * @param source строка с набором параметров (String)
     * @return итоговая цена (double)
     */
    public double getTotalPrice(Map<String, Double> map, String source) {
        var cardDiscount = getCardDiscount(source);
        var fullPrice = getFullPrice(map);
        return cardDiscount != 0 ? fullPrice - (fullPrice / 100 * cardDiscount) : fullPrice;
    }

    /**
     * Формирование кассового чека в виде набора строк
     *
     * @param source строка с набором параметров (String)
     * @return List<String>
     */
    public List<String> generateFullReceipt(String source) {
        DecimalFormat dF = new DecimalFormat("0.00");
        var priceMap = getPriceMap(source);
        var amountMap = getAmountMap(source);
        var fullPrice = dF.format(getFullPrice(priceMap));
        var discount = dF.format(getCardDiscount(source));
        var totalPrice = dF.format(getTotalPrice(priceMap, source));

        List<String> receiptList = new ArrayList<>(priceMap.size() + 7);
        receiptList.add(LINE_SEPARATION);
        for (Map.Entry<String, Double> entry : priceMap.entrySet()) {
            receiptList.add(
                    String.format(
                            LINE_FORMAT,
                            entry.getKey() + "  x" + amountMap.get(entry.getKey()),
                            dF.format(entry.getValue()) + "$"
                    )
            );
        }
        receiptList.add(LINE_SEPARATION);
        receiptList.add(String.format(LINE_FORMAT, "сумма", fullPrice + "$"));
        receiptList.add(String.format(LINE_FORMAT, "скидка", discount + "%"));
        receiptList.add(LINE_SEPARATION);
        receiptList.add(String.format(LINE_FORMAT, "итого к оплате", totalPrice + "$"));
        receiptList.add(LINE_SEPARATION);
        return receiptList;
    }
}
