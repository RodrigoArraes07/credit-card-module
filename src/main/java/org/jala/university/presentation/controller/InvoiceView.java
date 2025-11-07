package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.jala.university.application.dto.InvoiceCreateDTO;
import org.jala.university.application.dto.InvoiceDto;
import org.jala.university.application.service.InvoiceService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceView {

    private InvoiceService invoiceService;

    @FXML
    private DatePicker closingDatePicker;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private TextField totalAmountField;
    @FXML
    private CheckBox isPaidCheckBox;
    @FXML
    private ListView<InvoiceDto> invoiceList;

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
        if (invoiceService == null)
            return;

        InvoiceCreateDTO invoice = new InvoiceCreateDTO();
        invoice.setTotalAmount(Double.valueOf(totalAmountField.getText()));
        invoice.setClosingDate(closingDatePicker.getValue());
        invoice.setDueDate(dueDatePicker.getValue());
        invoice.setIsPaid(isPaidCheckBox.isSelected());
        // Preencha creditCard se necessário

        invoiceService.create(invoice);
        refreshInvoiceList();
    }

    private void refreshInvoiceList() {
        if (invoiceService == null)
            return;

        List<InvoiceDto> invoices = invoiceService.findAll();

        invoiceList.getItems().setAll(invoices);
    }
}
