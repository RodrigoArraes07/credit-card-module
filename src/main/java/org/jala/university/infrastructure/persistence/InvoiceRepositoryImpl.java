package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.Invoice;
import org.jala.university.domain.repository.InvoiceRepository;

import java.util.UUID;
/**
 * Implementação do repositório para a entidade Invoice.
 * Responsável por operações específicas de persistência da entidade.
 *
 * @author Hudson
 * @version 1.0
 */

public class InvoiceRepositoryImpl extends CrudRepository<Invoice, UUID> implements InvoiceRepository {

    public InvoiceRepositoryImpl(EntityManager entityManager) {
        super(Invoice.class, entityManager);
    }
}
