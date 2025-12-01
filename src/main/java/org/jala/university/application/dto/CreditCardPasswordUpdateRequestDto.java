package org.jala.university.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardPasswordUpdateRequestDto {
    private UUID userId;
    private String userLoginPassword;
    private String cardNumber;
    private String newPassword;
    private String confirmNewPassword;
}
