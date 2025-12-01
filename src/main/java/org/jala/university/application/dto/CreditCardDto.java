package org.jala.university.application.dto;

import lombok.*;
import org.jala.university.domain.enums.CreditCardStatus;
import org.jala.university.domain.enums.TargetType;

import java.io.Serial;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
public class CreditCardDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID credit_card_id;
    private String cardNumber;
    private String maskCardNumber;
    private String holderName;
    private Double limitAmount;
    private Double maxLimit;
    private TargetType targetType;
    private Integer cvc;
    private String passwordHash;
    private CreditCardStatus status;
    private Integer dueDay;
}
