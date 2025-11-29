package org.jala.university.application.service;

import org.jala.university.application.dto.PurchaseDto;
import org.jala.university.application.mapper.PurchaseMapper;
import org.jala.university.domain.entity.Purchase;
import org.jala.university.domain.repository.PurchaseRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, PurchaseMapper purchaseMapper) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
    }

    @Override
    public List<PurchaseDto> findByCreditCardId(UUID creditCardId) {
        List<Purchase> purchases = purchaseRepository.findByCreditCardId(creditCardId);
        return purchases.stream()
                .map(purchaseMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseDto> findRecentByCreditCardId(UUID creditCardId, int limit) {
        List<Purchase> purchases = purchaseRepository.findByCreditCardId(creditCardId);
        return purchases.stream()
                .sorted(Comparator.comparing(Purchase::getPurchaseDate).reversed())
                .limit(limit)
                .map(purchaseMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseDto findById(UUID id) {
        Purchase purchase = purchaseRepository.findById(id);
        return purchase != null ? purchaseMapper.mapTo(purchase) : null;
    }

    @Override
    public PurchaseDto create(PurchaseDto purchaseDto) {
        // Lógica de criação a ser implementada /correção
        return null;
    }

    @Override
    public PurchaseDto update(UUID id, PurchaseDto purchaseDto) {
        // Lógica de atualização a ser implementada /correção
        return null;
    }

    @Override
    public void deleteById(UUID id) {
        purchaseRepository.deleteById(id); //logica de exclusão /correção
    }
}
