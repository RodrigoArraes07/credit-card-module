package org.jala.university.domain.repository;

import org.jala.university.commons.domain.repository.Repository;
import org.jala.university.domain.entity.CreditCard;

import java.util.UUID;

public interface CreditCardRepository extends Repository<CreditCard, UUID> {
    CreditCard findByCardNumber(String cardNumber);
}
