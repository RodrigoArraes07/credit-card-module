package org.jala.university.domain.repository;

import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.Invoice;
import org.jala.university.commons.domain.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends Repository<Invoice, UUID> {
    List<Invoice> findFutureInvoicesByCreditCard(CreditCard creditCard, LocalDate currentDate);
}
