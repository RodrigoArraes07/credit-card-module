package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditCardCreateDto {
    private String password;
}
