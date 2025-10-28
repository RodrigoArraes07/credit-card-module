package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.jala.university.application.service.InvoiceService;
import org.jala.university.domain.entity.Invoice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceView {

    private InvoiceService invoiceService;

    @FXML
    private TextField totalAmountField;
    @FXML
    private DatePicker closingDatePicker;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private CheckBox paidCheckBox;
    @FXML
    private ListView<Invoice> invoiceList;

    public InvoiceView() {
    }

    public InvoiceView(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @FXML
    public void initialize() {
        if (invoiceService != null) {
            refreshInvoiceList();
        }
    }

    @FXML
    public void createInvoice() {
        if (invoiceService == null) return;

        Invoice invoice = new Invoice();
        invoice.setTotalAmount(Double.valueOf(totalAmountField.getText()));
        invoice.setClosingDate(closingDatePicker.getValue());
        invoice.setDueDate(dueDatePicker.getValue());
        invoice.setIsPaid(paidCheckBox.isSelected());
        // Preencha creditCard se necessário

        invoiceService.createInvoice(invoice);
        refreshInvoiceList();
    }

    private void refreshInvoiceList() {
        if (invoiceService == null) return;

        List<Invoice> invoices = invoiceService.getAllInvoices();
        invoiceList.getItems().setAll(invoices);
    }
}
