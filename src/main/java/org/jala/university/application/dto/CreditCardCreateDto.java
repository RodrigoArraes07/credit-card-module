package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;

@Data
@Builder
public class CreditCardCreateDto {
    private String cardNumber;
    private String holderName;
    private YearMonth expiration;
    private Double limitAmount;
    private Integer cvc;
    private String password;
}
