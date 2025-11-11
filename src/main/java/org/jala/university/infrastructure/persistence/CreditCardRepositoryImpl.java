package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.repository.CreditCardRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CreditCardRepositoryImpl
        extends CrudRepository<CreditCard, UUID>
        implements CreditCardRepository {

    public CreditCardRepositoryImpl(EntityManager entityManager) {
        super(CreditCard.class, entityManager);
    }

    @Override
    public CreditCard findByCardNumber(String cardNumber) {
        try {
            return getEntityManager()
                    .createQuery("SELECT c FROM CreditCard c WHERE c.cardNumber = :cardNumber", CreditCard.class)
                    .setParameter("cardNumber", cardNumber)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<CreditCard> findByUserId(UUID userId) {
        try {
            return getEntityManager()
                    .createQuery("SELECT c FROM CreditCard c WHERE c.user.id = :userId", CreditCard.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
