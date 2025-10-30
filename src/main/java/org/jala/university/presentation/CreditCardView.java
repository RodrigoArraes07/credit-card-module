package org.jala.university.presentation;

import lombok.Getter;
import org.jala.university.commons.presentation.View;

@Getter
public enum CreditCardView {
    MAIN("main-view.fxml"),
    CREDIT_CARD_REQUEST("credit-card-request-view.fxml");

    private final View view;

    CreditCardView(String fileName) {
        this.view = new View(fileName);
    }

}
