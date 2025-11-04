package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO utilizado para transferência de dados da entidade Purchase.
 * Campos confidenciais e identificadores sensíveis foram omitidos.
 *
 * @author Hudson Silva
 * @version 1.0
 */
@Builder
@Getter
@Setter
@ToString
public class PurchaseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String merchant;
    private String description;
    private Double totalAmount;
    private LocalDateTime purchaseDate;
    private Integer totalInstallments;
    private String category;
}

