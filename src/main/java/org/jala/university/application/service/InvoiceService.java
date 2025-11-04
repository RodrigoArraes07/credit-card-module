package org.jala.university.application.service;

import org.jala.university.application.dto.InvoiceCreateDTO;
import org.jala.university.application.dto.InvoiceDto;
import org.jala.university.domain.entity.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    List<InvoiceDto> findAll();
    InvoiceDto findById(UUID id);
    Invoice create(InvoiceCreateDTO invoiceDto);
    InvoiceDto update(UUID id, InvoiceDto invoiceDto);
    void deleteById(UUID id);
}
