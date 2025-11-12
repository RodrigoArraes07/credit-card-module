package org.jala.university.presentation;

import lombok.Getter;
import org.jala.university.commons.presentation.View;

@Getter
public enum CreditCardView {
    LOGIN("login-view.fxml"),
    HOME("home-view.fxml"),
    CARD_TYPE_SELECTION("card-type-selection-view.fxml"),
    REQUEST_CARD("credit-card-request-view.fxml"),
    MAIN("main-view.fxml");

    private final View view;

    CreditCardView(String fileName) {
        this.view = new View(fileName);
    }

}
