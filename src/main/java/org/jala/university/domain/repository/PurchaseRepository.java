package org.jala.university.domain.repository;

import org.jala.university.commons.domain.repository.Repository;
import org.jala.university.domain.entity.Purchase;

import java.util.UUID;

/**
 * Implementação do repositório para a entidade Invoice.
 * Responsável por operações de persistência específicas para Invoice.
 *
 * @author Hudson
 * @version 1.0
 */

public interface PurchaseRepository extends Repository<Purchase, UUID> {
    Purchase findByID(UUID uuid);
}