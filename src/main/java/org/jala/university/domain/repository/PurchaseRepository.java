package org.jala.university.domain.repository;

import org.jala.university.commons.domain.repository.Repository;
import org.jala.university.domain.entity.Purchase;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PurchaseRepository extends Repository<Purchase, UUID> {
    List<Purchase> findByCreditCardId(UUID creditCardId);
    List<Purchase> findByCreditCardIdAndDateRange(UUID creditCardId, LocalDate startDate, LocalDate endDate);
}
