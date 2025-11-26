package org.jala.university.presentation.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jala.university.commons.presentation.ViewContext;
import org.jala.university.domain.enums.CreditCardStatus;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CardSecurityContext implements ViewContext {
    private final UUID userId;
    private final String cardNumber;
    private final String maskCardNumber;
    private final CreditCardStatus currentStatus;
}
