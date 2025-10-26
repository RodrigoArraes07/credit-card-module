package org.jala.university.application.service;

import org.jala.university.domain.entity.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    Invoice createInvoice(Invoice invoice);

    List<Invoice> getAllInvoices();

    Optional<Invoice> getInvoiceById(Long id);
}
