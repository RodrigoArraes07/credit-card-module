package org.jala.university.domain.repository;

import org.jala.university.domain.entity.Invoice;
import org.jala.university.commons.domain.repository.Repository;

import java.util.UUID;

public interface InvoiceRepository extends Repository<Invoice, UUID> {
}
