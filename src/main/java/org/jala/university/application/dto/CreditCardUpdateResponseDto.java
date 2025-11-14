package org.jala.university.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardUpdateResponseDto {
    private String maskCardNumber;
    private Double updatedLimit;
    private Integer updatedDueDay;
    private Integer affectedInvoices;
    private String message;
    private Boolean success;
}
