package org.jala.university.presentation.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jala.university.commons.presentation.ViewContext;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CardUpdateContext implements ViewContext {
    private final UUID userId;
    private final String cardNumber;
    private final String maskCardNumber;
}
