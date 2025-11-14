package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jala.university.domain.enums.InstallmentStatus;
import org.jala.university.domain.enums.PurchaseCategory;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
public class PurchaseInstallmentDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Integer installmentNumber;
    private Double installmentAmount;
    private LocalDateTime dueDate;
    private InstallmentStatus status;
    private String merchant;
    private String description;
    private Double totalAmount;
    private Integer totalInstallments;
    private PurchaseCategory category;
    private LocalDateTime purchaseDate;
}
