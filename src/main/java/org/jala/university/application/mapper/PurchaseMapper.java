package org.jala.university.application.mapper;

import org.jala.university.application.dto.PurchaseDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.Purchase;
import org.jala.university.domain.enums.PurchaseCategory;

public class PurchaseMapper implements Mapper<Purchase, PurchaseDto> {

    private static PurchaseMapper instance;

    private PurchaseMapper() {}

    public static PurchaseMapper getInstance() {
        if (instance == null) {
            instance = new PurchaseMapper();
        }
        return instance;
    }

    @Override
    public PurchaseDto mapTo(Purchase entity) {
        if (entity == null) {
            return null;
        }
        return PurchaseDto.builder()
                .merchant(entity.getMerchant())
                .description(entity.getDescription())
                .totalAmount(entity.getTotalAmount())
                .purchaseDate(entity.getPurchaseDate())
                .totalInstallments(entity.getTotalInstallments())
                .category(entity.getCategory() != null ? entity.getCategory().name() : null)
                .build();
    }

    @Override
    public Purchase mapFrom(PurchaseDto dto) {
        if (dto == null) {
            return null;
        }
        return Purchase.builder()
                .merchant(dto.getMerchant())
                .description(dto.getDescription())
                .totalAmount(dto.getTotalAmount())
                .purchaseDate(dto.getPurchaseDate())
                .totalInstallments(dto.getTotalInstallments())
                .category(dto.getCategory() != null ? PurchaseCategory.valueOf(dto.getCategory()) : null)
                .build();
    }
}
