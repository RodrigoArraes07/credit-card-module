package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.Invoice;
import org.jala.university.domain.repository.InvoiceRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class InvoiceRepositoryImpl
        extends CrudRepository<Invoice, UUID>
        implements InvoiceRepository {

    public InvoiceRepositoryImpl(EntityManager entityManager) {
        super(Invoice.class, entityManager);
    }

    @Override
    public List<Invoice> findByCreditCardId(UUID creditCardId) {
        try {
            return getEntityManager()
                    .createQuery("SELECT i FROM Invoice i WHERE i.creditCardId.id = :creditCardId ORDER BY i.closingDate DESC", Invoice.class)
                    .setParameter("creditCardId", creditCardId)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public List<Invoice> findByCreditCardIdAndDateRange(UUID creditCardId, LocalDate startDate, LocalDate endDate) {
        try {
            return getEntityManager()
                    .createQuery("SELECT i FROM Invoice i WHERE i.creditCardId.id = :creditCardId AND i.closingDate BETWEEN :startDate AND :endDate ORDER BY i.closingDate DESC", Invoice.class)
                    .setParameter("creditCardId", creditCardId)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public Invoice findByCreditCardIdAndClosingDate(UUID creditCardId, LocalDate closingDate) {
        try {
            return getEntityManager()
                    .createQuery("SELECT i FROM Invoice i WHERE i.creditCardId.id = :creditCardId AND i.closingDate = :closingDate", Invoice.class)
                    .setParameter("creditCardId", creditCardId)
                    .setParameter("closingDate", closingDate)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
