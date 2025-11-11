package org.jala.university.application.service;

import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.dto.CreditCardRequestDto;
import org.jala.university.application.dto.CreditCardResponseDto;
import org.jala.university.domain.entity.CreditCard;

import java.util.List;
import java.util.UUID;

public interface CreditCardService {
    List<CreditCardDto> findAll();
    CreditCardDto findById(UUID id);
    CreditCardDto create(CreditCard creditCard);
    CreditCardDto update(UUID id, CreditCard creditCard);
    void deleteById(UUID id);
    CreditCardResponseDto requestCreditCard(CreditCardRequestDto request);
    Double calculatePreApprovedLimit(Double monthlyIncome);
    boolean blockCard(UUID cardId, UUID userId);
    boolean unblockCard(UUID cardId, UUID userId);
    boolean cancelCard(UUID cardId, UUID userId);
    CreditCardDto blockCardWithMessage(UUID cardId, UUID userId);
    CreditCardDto unblockCardWithMessage(UUID cardId, UUID userId);
    CreditCardDto cancelCardWithMessage(UUID cardId, UUID userId);
}
