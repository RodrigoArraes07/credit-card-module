package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;

@Data
@Builder
public class CreditCardCreateDto {
    private String password;
}
