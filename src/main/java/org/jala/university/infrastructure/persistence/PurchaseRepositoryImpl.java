package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.Purchase;
import org.jala.university.domain.repository.PurchaseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PurchaseRepositoryImpl
        extends CrudRepository<Purchase, UUID>
        implements PurchaseRepository {

    public PurchaseRepositoryImpl(EntityManager entityManager) {
        super(Purchase.class, entityManager);
    }

    @Override
    public List<Purchase> findByCreditCardId(UUID creditCardId) {
        try {
            return getEntityManager()
                    .createQuery("SELECT p FROM Purchase p WHERE p.creditCard.id = :creditCardId ORDER BY p.purchaseDate DESC", Purchase.class)
                    .setParameter("creditCardId", creditCardId)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public List<Purchase> findByCreditCardIdAndDateRange(UUID creditCardId, LocalDate startDate, LocalDate endDate) {
        try {
            return getEntityManager()
                    .createQuery("SELECT p FROM Purchase p WHERE p.creditCard.id = :creditCardId AND p.purchaseDate BETWEEN :startDate AND :endDate ORDER BY p.purchaseDate DESC", Purchase.class)
                    .setParameter("creditCardId", creditCardId)
                    .setParameter("startDate", startDate.atStartOfDay())
                    .setParameter("endDate", endDate.atTime(23, 59, 59))
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }
}
