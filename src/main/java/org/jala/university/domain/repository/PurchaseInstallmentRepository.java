package org.jala.university.domain.repository;

import org.jala.university.commons.domain.repository.Repository;
import org.jala.university.domain.entity.PurchaseInstallment;
import org.jala.university.domain.enums.InstallmentStatus;
import org.jala.university.domain.enums.PurchaseCategory;

import java.util.List;
import java.util.UUID;

public interface PurchaseInstallmentRepository extends Repository<PurchaseInstallment, UUID> {
    List<PurchaseInstallment> findByInvoiceId(UUID invoiceId);
    List<PurchaseInstallment> findByInvoiceIdAndStatus(UUID invoiceId, InstallmentStatus status);
    List<PurchaseInstallment> findByInvoiceIdAndCategory(UUID invoiceId, PurchaseCategory category);
}
