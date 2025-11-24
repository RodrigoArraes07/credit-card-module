package org.jala.university.application.service;

import org.jala.university.application.dto.PurchaseDto;
import org.jala.university.domain.entity.Purchase;

import java.util.List;
import java.util.UUID;

public interface PurchaseService {
    List<PurchaseDto> findByCreditCardId(UUID creditCardId);

    List<PurchaseDto> findRecentByCreditCardId(UUID creditCardId, int limit);

    PurchaseDto findById(UUID id);

    PurchaseDto create(PurchaseDto purchaseDto);

    PurchaseDto update(UUID id, PurchaseDto purchaseDto);

    void deleteById(UUID id);
}
