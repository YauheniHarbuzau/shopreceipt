package com.example.shopreceipt.util;

import com.example.shopreceipt.entity.Card;
import com.example.shopreceipt.entity.Product;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static com.example.shopreceipt.constants.Constants.FILE_PATH;
import static com.example.shopreceipt.constants.Constants.XML_CARDS;
import static com.example.shopreceipt.constants.Constants.XML_PRODUCTS;

/**
 * Class for creating xml files
 */
public class XmlCreating {

    private static Document document = new Document();
    private static Element products = new Element("products");
    private static Element cards = new Element("cards");

    public static void addXml(Object obj, String operation) throws IOException {
        if (obj instanceof Product product) {
            Element child = new Element("product");
            child.addContent(new Element("operation").setText(operation));
            child.addContent(new Element("timestamp").setText(String.valueOf(Date.from(Instant.now()))));
            child.addContent(new Element("id").setText(String.valueOf(Optional.of(product.getId()))));
            child.addContent(new Element("name").setText(String.valueOf(Optional.of(product.getName()))));
            child.addContent(new Element("price").setText(String.valueOf(Optional.of(product.getPrice()))));
            child.addContent(new Element("promotion").setText(String.valueOf(Optional.of(product.getPromotion()))));

            products.addContent(child);
            document.setContent(products);
            writeFile(FILE_PATH + XML_PRODUCTS);
        }
        if (obj instanceof Card card) {
            Element child = new Element("card");
            child.addContent(new Element("operation").setText(operation));
            child.addContent(new Element("timestamp").setText(String.valueOf(Date.from(Instant.now()))));
            child.addContent(new Element("id").setText(String.valueOf(Optional.of(card.getId()))));
            child.addContent(new Element("number").setText(String.valueOf(card.getNumber())));
            child.addContent(new Element("discount").setText(String.valueOf(card.getDiscount())));

            cards.addContent(child);
            document.setContent(cards);
            writeFile(FILE_PATH + XML_CARDS);
        }
    }

    private static void writeFile(String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        outputter.output(document, fileWriter);
        outputter.output(document, System.out);
    }
}
