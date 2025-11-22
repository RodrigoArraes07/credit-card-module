package org.jala.university.application.service;

import org.jala.university.application.dto.PurchaseInstallmentDto;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing purchase installments.
 */
public interface PurchaseInstallmentService {
    /**
     * Finds all purchase installments associated with a specific invoice.
     *
     * @param invoiceId The ID of the invoice.
     * @return A list of purchase installment DTOs.
     * @author Hudson
     */
    List<PurchaseInstallmentDto> findByInvoiceId(UUID invoiceId);
}
