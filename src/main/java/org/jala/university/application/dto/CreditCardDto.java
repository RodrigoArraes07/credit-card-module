package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Data;
import org.jala.university.domain.entity.CreditCardStatus;
import org.springframework.boot.jackson.JsonComponent;

import java.time.YearMonth;
import java.util.UUID;

@Data
@Builder
public class CreditCardDto {
    private UUID credit_card_id;
    private String cardNumber;
    private String holderName;
    private YearMonth expiration;
    private Double limitAmount;
    private Double balance;
    private Integer cvc;
    private CreditCardStatus status;
    }
