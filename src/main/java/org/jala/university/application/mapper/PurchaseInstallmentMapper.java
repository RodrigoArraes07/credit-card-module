package org.jala.university.application.mapper;

import org.jala.university.application.dto.PurchaseInstallmentDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.Purchase;
import org.jala.university.domain.entity.PurchaseInstallment;

public class PurchaseInstallmentMapper implements Mapper<PurchaseInstallment, PurchaseInstallmentDto> {

    private static PurchaseInstallmentMapper instance;

    private PurchaseInstallmentMapper() {}

    public static PurchaseInstallmentMapper getInstance() {
        if (instance == null) {
            instance = new PurchaseInstallmentMapper();
        }
        return instance;
    }

    @Override
    public PurchaseInstallmentDto mapTo(PurchaseInstallment entity) {
        if (entity == null) {
            return null;
        }

        Purchase purchase = entity.getPurchase();

        return PurchaseInstallmentDto.builder()
                .id(entity.getId())
                .installmentNumber(entity.getInstallmentNumber())
                .installmentAmount(entity.getInstallmentAmount())
                .dueDate(entity.getDueDate())
                .status(entity.getStatus())
                .merchant(purchase != null ? purchase.getMerchant() : null)
                .description(purchase != null ? purchase.getDescription() : null)
                .totalAmount(purchase != null ? purchase.getTotalAmount() : null)
                .totalInstallments(purchase != null ? purchase.getTotalInstallments() : null)
                .category(purchase != null ? purchase.getCategory() : null)
                .purchaseDate(purchase != null ? purchase.getPurchaseDate() : null)
                .build();
    }

    @Override
    public PurchaseInstallment mapFrom(PurchaseInstallmentDto dto) {
        if (dto == null) {
            return null;
        }

        return PurchaseInstallment.builder()
                .installmentNumber(dto.getInstallmentNumber())
                .installmentAmount(dto.getInstallmentAmount())
                .dueDate(dto.getDueDate())
                .status(dto.getStatus())
                .build();
    }
}
