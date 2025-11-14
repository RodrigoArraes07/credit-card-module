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
