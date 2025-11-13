package org.jala.university.domain.repository;

import org.jala.university.domain.entity.Invoice;
import org.jala.university.commons.domain.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends Repository<Invoice, UUID> {
    List<Invoice> findByCreditCardId(UUID creditCardId);
    List<Invoice> findByCreditCardIdAndDateRange(UUID creditCardId, LocalDate startDate, LocalDate endDate);
    Invoice findByCreditCardIdAndClosingDate(UUID creditCardId, LocalDate closingDate);
    Invoice findCurrentInvoice(UUID creditCardId, LocalDate referenceDate);
    Invoice findPreviousInvoice(UUID creditCardId, LocalDate referenceDate);
    Invoice findNextInvoice(UUID creditCardId, LocalDate referenceDate);
    List<Invoice> findByYear(UUID creditCardId, int year);
}
