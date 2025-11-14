package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.Invoice;
import org.jala.university.domain.repository.InvoiceRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class InvoiceRepositoryImpl extends CrudRepository<Invoice, UUID> implements InvoiceRepository {

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

    @Override
    public Invoice findCurrentInvoice(UUID creditCardId, LocalDate referenceDate) {
        try {
            return getEntityManager()
                    .createQuery(
                            "SELECT i FROM Invoice i " +
                            "WHERE i.creditCardId.id = :creditCardId " +
                            "AND i.closingDate <= :referenceDate " +
                            "AND i.isPaid = false " +
                            "ORDER BY i.closingDate DESC",
                            Invoice.class
                    )
                    .setParameter("creditCardId", creditCardId)
                    .setParameter("referenceDate", referenceDate)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Invoice findPreviousInvoice(UUID creditCardId, LocalDate referenceDate) {
        try {
            return getEntityManager()
                    .createQuery(
                            "SELECT i FROM Invoice i " +
                            "WHERE i.creditCardId.id = :creditCardId " +
                            "AND i.closingDate < :referenceDate " +
                            "ORDER BY i.closingDate DESC",
                            Invoice.class
                    )
                    .setParameter("creditCardId", creditCardId)
                    .setParameter("referenceDate", referenceDate)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Invoice findNextInvoice(UUID creditCardId, LocalDate referenceDate) {
        try {
            return getEntityManager()
                    .createQuery(
                            "SELECT i FROM Invoice i " +
                            "WHERE i.creditCardId.id = :creditCardId " +
                            "AND i.closingDate > :referenceDate " +
                            "ORDER BY i.closingDate ASC",
                            Invoice.class
                    )
                    .setParameter("creditCardId", creditCardId)
                    .setParameter("referenceDate", referenceDate)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Invoice> findByYear(UUID creditCardId, int year) {
        try {
            LocalDate startOfYear = LocalDate.of(year, 1, 1);
            LocalDate endOfYear = LocalDate.of(year, 12, 31);
            return getEntityManager()
                    .createQuery(
                            "SELECT i FROM Invoice i " +
                            "WHERE i.creditCardId.id = :creditCardId " +
                            "AND i.closingDate BETWEEN :startDate AND :endDate " +
                            "ORDER BY i.closingDate ASC",
                            Invoice.class
                    )
                    .setParameter("creditCardId", creditCardId)
                    .setParameter("startDate", startOfYear)
                    .setParameter("endDate", endOfYear)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public List<Invoice> findFutureInvoicesByCreditCard(CreditCard creditCard, LocalDate currentDate) {
        try {
            String jpql = "SELECT i FROM Invoice i WHERE i.creditCardId = :creditCard AND i.dueDate > :currentDate AND i.isPaid = false";
            TypedQuery<Invoice> query = getEntityManager().createQuery(jpql, Invoice.class);
            query.setParameter("creditCard", creditCard);
            query.setParameter("currentDate", currentDate);
            return query.getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }
}
