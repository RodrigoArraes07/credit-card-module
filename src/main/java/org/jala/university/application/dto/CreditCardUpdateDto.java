package org.jala.university.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardUpdateDto {
    private String cardNumber;
    private Double newLimitAmount;
    private Integer newDueDay;
}
