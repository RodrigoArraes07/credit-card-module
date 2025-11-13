package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.PurchaseInstallment;
import org.jala.university.domain.enums.InstallmentStatus;
import org.jala.university.domain.enums.PurchaseCategory;
import org.jala.university.domain.repository.PurchaseInstallmentRepository;

import java.util.List;
import java.util.UUID;

public class PurchaseInstallmentRepositoryImpl
        extends CrudRepository<PurchaseInstallment, UUID>
        implements PurchaseInstallmentRepository {

    public PurchaseInstallmentRepositoryImpl(EntityManager entityManager) {
        super(PurchaseInstallment.class, entityManager);
    }

    @Override
    public List<PurchaseInstallment> findByInvoiceId(UUID invoiceId) {
        try {
            return getEntityManager()
                    .createQuery(
                            "SELECT pi FROM PurchaseInstallment pi " +
                            "JOIN FETCH pi.purchase p " +
                            "WHERE pi.invoice.id = :invoiceId " +
                            "ORDER BY pi.dueDate ASC",
                            PurchaseInstallment.class
                    )
                    .setParameter("invoiceId", invoiceId)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public List<PurchaseInstallment> findByInvoiceIdAndStatus(UUID invoiceId, InstallmentStatus status) {
        try {
            return getEntityManager()
                    .createQuery(
                            "SELECT pi FROM PurchaseInstallment pi " +
                            "JOIN FETCH pi.purchase p " +
                            "WHERE pi.invoice.id = :invoiceId " +
                            "AND pi.status = :status " +
                            "ORDER BY pi.dueDate ASC",
                            PurchaseInstallment.class
                    )
                    .setParameter("invoiceId", invoiceId)
                    .setParameter("status", status)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public List<PurchaseInstallment> findByInvoiceIdAndCategory(UUID invoiceId, PurchaseCategory category) {
        try {
            return getEntityManager()
                    .createQuery(
                            "SELECT pi FROM PurchaseInstallment pi " +
                            "JOIN FETCH pi.purchase p " +
                            "WHERE pi.invoice.id = :invoiceId " +
                            "AND p.category = :category " +
                            "ORDER BY pi.dueDate ASC",
                            PurchaseInstallment.class
                    )
                    .setParameter("invoiceId", invoiceId)
                    .setParameter("category", category)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }
}
