package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class CreditCardRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String holderName;
    private String cpf;
    private Double monthlyIncome;
    private String email;
    private String password;
}