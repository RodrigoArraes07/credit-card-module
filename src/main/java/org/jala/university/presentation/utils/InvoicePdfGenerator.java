package org.jala.university.presentation.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jala.university.application.dto.InvoiceDetailDto;
import org.jala.university.application.dto.PurchaseInstallmentDto;

import java.io.FileOutputStream;

public class InvoicePdfGenerator {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.GRAY);

    private InvoicePdfGenerator() {
    }

    public static void generateInvoicePdf(InvoiceDetailDto invoice, String outputPath) throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        addTitle(document);
        addInvoiceInfo(document, invoice);
        addSeparator(document);
        addPurchasesTable(document, invoice);
        addTotal(document, invoice);

        document.close();
    }

    private static void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("EXTRATO DE FATURA", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        Paragraph subtitle = new Paragraph("Cartão de Crédito", NORMAL_FONT);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);
    }

    private static void addInvoiceInfo(Document document, InvoiceDetailDto invoice) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(15);

        addInfoRow(table, "Data de Fechamento:", DateFormatter.formatShortDate(invoice.getClosingDate()));
        addInfoRow(table, "Data de Vencimento:", DateFormatter.formatShortDate(invoice.getDueDate()));
        addInfoRow(table, "Valor Total:", CurrencyFormatter.format(invoice.getTotalAmount()));
        addInfoRow(table, "Status:", invoice.getIsPaid() ? "PAGA" : "EM ABERTO");

        document.add(table);
    }

    private static void addInfoRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, HEADER_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPaddingBottom(8);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPaddingBottom(8);
        table.addCell(valueCell);
    }

    private static void addSeparator(Document document) throws DocumentException {
        Paragraph separator = new Paragraph("COMPRAS E PARCELAS", HEADER_FONT);
        separator.setAlignment(Element.ALIGN_LEFT);
        separator.setSpacingBefore(10);
        separator.setSpacingAfter(10);
        document.add(separator);
    }

    private static void addPurchasesTable(Document document, InvoiceDetailDto invoice) throws DocumentException {
        if (invoice.getInstallments() == null || invoice.getInstallments().isEmpty()) {
            Paragraph empty = new Paragraph("Nenhuma compra registrada nesta fatura.", NORMAL_FONT);
            empty.setSpacingAfter(20);
            document.add(empty);
            return;
        }

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3f, 2f, 2f, 2f});
        table.setSpacingAfter(20);

        addTableHeader(table, "Estabelecimento");
        addTableHeader(table, "Data");
        addTableHeader(table, "Parcela");
        addTableHeader(table, "Valor");

        for (PurchaseInstallmentDto installment : invoice.getInstallments()) {
            addTableCell(table, installment.getMerchant() != null ? installment.getMerchant() : "N/A");
            addTableCell(table, DateFormatter.formatShortDate(installment.getPurchaseDate()));

            String installmentInfo = installment.getTotalInstallments() > 1 ?
                    installment.getInstallmentNumber() + "/" + installment.getTotalInstallments() :
                    "À vista";
            addTableCell(table, installmentInfo);

            addTableCell(table, CurrencyFormatter.format(installment.getInstallmentAmount()));
        }

        document.add(table);
    }

    private static void addTableHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(new BaseColor(230, 230, 230));
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private static void addTableCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setPadding(6);
        table.addCell(cell);
    }

    private static void addTotal(Document document, InvoiceDetailDto invoice) throws DocumentException {
        Paragraph total = new Paragraph("TOTAL DA FATURA: " + CurrencyFormatter.format(invoice.getTotalAmount()), HEADER_FONT);
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(10);
        document.add(total);

        Paragraph footer = new Paragraph("Documento gerado automaticamente", SMALL_FONT);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(30);
        document.add(footer);
    }
}
