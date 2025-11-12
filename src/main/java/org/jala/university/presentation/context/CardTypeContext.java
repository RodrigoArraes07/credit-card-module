package org.jala.university.presentation.context;

import lombok.Getter;
import org.jala.university.commons.presentation.ViewContext;
import org.jala.university.domain.enums.CardType;

@Getter
public class CardTypeContext implements ViewContext {
    private final CardType cardType;

    public CardTypeContext(CardType cardType) {
        this.cardType = cardType;
    }
}
