package org.jala.university.application.mapper;

import org.jala.university.domain.entity.Purchase;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.enums.PurchaseCategory;
import org.jala.university.application.dto.PurchaseDto;

/**
 * Mapper para converter Purchase <-> PurchaseDto.
 *
 * @author Hudson Silva
 * @version 1.0
 */
public class PurchaseMapper {

    public static PurchaseDto toDto(Purchase purchase) {
        if (purchase == null) return null;
        return PurchaseDto.builder()
                .merchant(purchase.getMerchant())
                .description(purchase.getDescription())
                .totalAmount(purchase.getTotalAmount())
                .purchaseDate(purchase.getPurchaseDate())
                .totalInstallments(purchase.getTotalInstallments())
                .category(
                        purchase.getCategory() != null ? purchase.getCategory().name() : null
                )
                .build();
    }

    public static Purchase toEntity(PurchaseDto dto, CreditCard creditCard) {
        if (dto == null) return null;
        return Purchase.builder()
                .creditCard(creditCard)
                .merchant(dto.getMerchant())
                .description(dto.getDescription())
                .totalAmount(dto.getTotalAmount())
                .purchaseDate(dto.getPurchaseDate())
                .totalInstallments(dto.getTotalInstallments())
                .category(dto.getCategory() != null ? PurchaseCategory.valueOf(dto.getCategory()) : null)
                .build();
    }
}
