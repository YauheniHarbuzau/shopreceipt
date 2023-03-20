package com.example.shopreceipt.util;

import com.example.shopreceipt.service.ReceiptService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import static com.example.shopreceipt.constants.Constants.FILE_PATH;
import static com.example.shopreceipt.constants.Constants.PDF_RECEIPT;

/**
 * Class for creating pdf files
 */
@AllArgsConstructor
@Component
public class PdfCreating {

    private final ReceiptService receiptService;

    public void addPdf(String source) throws IOException {
        DecimalFormat dF = new DecimalFormat("0.00");
        var priceMap = receiptService.getPriceMap(source);
        var amountMap = receiptService.getAmountMap(source);
        var fullPrice = dF.format(receiptService.getFullPrice(priceMap));
        var discount = dF.format(receiptService.getCardDiscount(source));
        var totalPrice = dF.format(receiptService.getTotalPrice(priceMap, source));

        PdfReader reader = new PdfReader("resources/pdftemplate/Clevertec_Template.pdf");
        PdfWriter writer = new PdfWriter(FILE_PATH + PDF_RECEIPT);
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        Document doc = new Document(pdfDoc);

        for (int i = 0; i < 4; i++) {
            doc.add(new Paragraph("\n"));
        }
        doc.add(createInfoTable());
        doc.add(new Paragraph("\n"));
        doc.add(createProductTable(priceMap, amountMap));
        doc.add(new Paragraph("\n"));
        doc.add(createTotalPriceTable(fullPrice, discount, totalPrice));

        doc.close();
    }

    private static Table createInfoTable() {
        Table table = new Table(new float[]{120F, 100F, 100F, 80F});
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(putBigCell("CASH RECEIPT", 20F, TextAlignment.CENTER, 1, 4));
        table.addCell(putBigCell("PRODUCTMARKET", 16F, TextAlignment.CENTER, 1, 4));
        table.addCell(putBigCell("Oslo / Norway", 12F, TextAlignment.CENTER, 1, 4));
        table.addCell(putBigCell("Tel: 111-000-00", 12F, TextAlignment.CENTER, 1, 4));
        table.addCell(putBigCell("Date:", 12F, TextAlignment.RIGHT, 1, 3));
        table.addCell(putCell(String.valueOf(LocalDate.now()), 12F, TextAlignment.RIGHT));
        table.addCell(putBigCell("Time:", 12F, TextAlignment.RIGHT, 1, 3));
        table.addCell(putCell(String.valueOf(LocalTime.now().withNano(0)), 12F, TextAlignment.RIGHT));

        return table;
    }

    private static Table createProductTable(Map<String, Double> priceMap, Map<String, Integer> amountMap) {
        Table table = new Table(new float[]{200F, 100F, 100F});
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(putCell("product", 12F, TextAlignment.LEFT));
        table.addCell(putCell("amount", 12F, TextAlignment.LEFT));
        table.addCell(putCell("price", 12F, TextAlignment.RIGHT));

        DecimalFormat dF = new DecimalFormat("0.00");
        for (Map.Entry<String, Double> entry : priceMap.entrySet()) {
            table.addCell(putCell(entry.getKey(), 16F, TextAlignment.LEFT));
            table.addCell(putCell("x" + amountMap.get(entry.getKey()), 16F, TextAlignment.LEFT));
            table.addCell(putCell(dF.format(entry.getValue()) + "$", 16F, TextAlignment.RIGHT));
        }

        return table;
    }

    private static Table createTotalPriceTable(String fullPrice, String discount, String totalPrice) {
        Table table = new Table(new float[]{300F, 100F});
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(putCell("full price:", 16F, TextAlignment.LEFT));
        table.addCell(putCell(fullPrice + "$", 16F, TextAlignment.RIGHT));

        table.addCell(putCell("discount:", 16F, TextAlignment.LEFT));
        table.addCell(putCell(discount + "%", 16F, TextAlignment.RIGHT));

        table.addCell(putCell("total price:", 16F, TextAlignment.LEFT));
        table.addCell(putCell(totalPrice + "$", 16F, TextAlignment.RIGHT));

        return table;
    }

    private static Cell putCell(String text, Float fontSize, TextAlignment textAlignment) {
        return new Cell()
                .add(new Paragraph(text))
                .setFontSize(fontSize)
                .setTextAlignment(textAlignment)
                .setBorder(Border.NO_BORDER);
    }

    private static Cell putBigCell(String text, Float fontSize, TextAlignment textAlignment, int rowspan, int colspan) {
        return new Cell(rowspan, colspan)
                .add(new Paragraph(text))
                .setFontSize(fontSize)
                .setTextAlignment(textAlignment)
                .setBorder(Border.NO_BORDER);
    }
}
