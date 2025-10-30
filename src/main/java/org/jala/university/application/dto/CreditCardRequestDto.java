package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jala.university.domain.enums.CreditCardStatus;
import org.jala.university.domain.enums.TargetType;

import java.io.Serial;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.UUID;

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
