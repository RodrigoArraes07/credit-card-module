package org.jala.university.application.service;

import org.jala.university.application.dto.PurchaseDto;

import java.util.List;
import java.util.UUID;

public interface PurchaseService {
    List<PurchaseDto> findByCreditCardId(UUID creditCardId);
    List<PurchaseDto> findRecentByCreditCardId(UUID creditCardId, int limit);
}
