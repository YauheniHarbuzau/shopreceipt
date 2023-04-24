package com.example.shopreceipt.util;

import com.example.shopreceipt.entity.Receipt;
import com.example.shopreceipt.entity.ReceiptProduct;
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

import static com.example.shopreceipt.constants.Constants.FILE_PATH;
import static com.example.shopreceipt.constants.Constants.PDF_RECEIPT_FILE;

/**
 * Class for creating pdf files
 */
@AllArgsConstructor
@Component
public class PdfCreating {

    private final ReceiptService receiptService;

    public void addPdf(String source) throws IOException {
        var receipt = receiptService.getReceipt(source);

        PdfReader reader = new PdfReader("resources/pdftemplate/Clevertec_Template.pdf");
        PdfWriter writer = new PdfWriter(FILE_PATH + PDF_RECEIPT_FILE);
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        Document doc = new Document(pdfDoc);

        for (int i = 0; i < 4; i++) {
            doc.add(new Paragraph("\n"));
        }
        doc.add(createInfoTable());
        doc.add(new Paragraph("\n"));
        doc.add(createProductTable(receipt));
        doc.add(new Paragraph("\n"));
        doc.add(createTotalPriceTable(receipt));

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

    private static Table createProductTable(Receipt receipt) {
        Table table = new Table(new float[]{160F, 80F, 80F, 80F});
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        DecimalFormat dF = new DecimalFormat("0.00");
        var receiptProducts = receipt.getReceiptProducts();

        table.addCell(putCell("product", 12F, TextAlignment.LEFT));
        table.addCell(putCell("price", 12F, TextAlignment.RIGHT));
        table.addCell(putCell("amount", 12F, TextAlignment.RIGHT));
        table.addCell(putCell("full price", 12F, TextAlignment.RIGHT));

        for (ReceiptProduct receiptProduct : receiptProducts) {
            table.addCell(putCell(receiptProduct.getName(), 16F, TextAlignment.LEFT));
            table.addCell(putCell(dF.format(receiptProduct.getPrice()) + "$", 16F, TextAlignment.RIGHT));
            table.addCell(putCell("x" + receiptProduct.getAmount(), 16F, TextAlignment.RIGHT));
            table.addCell(putCell(dF.format(receiptProduct.getFullPrice()) + "$", 16F, TextAlignment.RIGHT));
        }

        return table;
    }

    private static Table createTotalPriceTable(Receipt receipt) {
        Table table = new Table(new float[]{320F, 80F});
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        DecimalFormat dF = new DecimalFormat("0.00");
        var fullPrice = dF.format(receipt.getFullPrice());
        var cardDiscount = dF.format(receipt.getCardDiscount());
        var totalPrice = dF.format(receipt.getTotalPrice());

        table.addCell(putCell("full price:", 16F, TextAlignment.LEFT));
        table.addCell(putCell(fullPrice + "$", 16F, TextAlignment.RIGHT));

        table.addCell(putCell("card discount:", 16F, TextAlignment.LEFT));
        table.addCell(putCell(cardDiscount + "%", 16F, TextAlignment.RIGHT));

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
