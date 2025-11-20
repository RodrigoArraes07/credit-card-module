package org.jala.university.application.service;

import org.jala.university.application.dto.InvoiceCreateDTO;
import org.jala.university.application.dto.InvoiceDetailDto;
import org.jala.university.application.dto.InvoiceDto;
import org.jala.university.application.dto.InvoiceTimelineDto;
import org.jala.university.application.dto.PaymentResponseDto;
import org.jala.university.domain.entity.Invoice;
import org.jala.university.domain.enums.PaymentMethod;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceService {
    List<InvoiceDto> findAll();
    static InvoiceDto findById(UUID id);
    Invoice create(InvoiceCreateDTO invoiceDto);
    InvoiceDto update(UUID id, InvoiceDto invoiceDto);
    void deleteById(UUID id);
    Optional<InvoiceTimelineDto> getInvoiceTimeline(UUID creditCardId);
    Optional<InvoiceDetailDto> getInvoiceDetail(UUID invoiceId);
    List<InvoiceDto> getInvoicesByYear(UUID creditCardId, int year);
    List<InvoiceDetailDto> getAllInvoicesByCreditCard(UUID creditCardId);
    PaymentResponseDto processPayment(UUID invoiceId, PaymentMethod method);
}
