package org.jala.university.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jala.university.domain.enums.CreditCardStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardStatusUpdateResponseDto {
    private String maskCardNumber;
    private CreditCardStatus status;
    private String message;
    private Boolean success;
}
