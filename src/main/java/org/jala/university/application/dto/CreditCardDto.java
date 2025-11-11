package org.jala.university.application.dto;

import lombok.*;
import org.jala.university.domain.enums.CreditCardStatus;
import org.jala.university.domain.enums.TargetType;

import java.io.Serial;
import java.io.Serializable;
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
    private Double limitAmount;
    private TargetType targetType;
    private Integer cvc;
    private CreditCardStatus status;
    private String message;

}
