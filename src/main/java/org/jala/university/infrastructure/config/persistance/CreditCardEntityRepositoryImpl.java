package org.jala.university.infrastructure.config.persistance;
import jakarta.persistence.EntityManager;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.repository.CreditCardRepository;
import java.util.UUID;
public class CreditCardEntityRepositoryImpl extends CrudRepository<CreditCard, UUID> implements CreditCardRepository {
    protected CreditCardEntityRepositoryImpl(EntityManager entityManager) {
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
}