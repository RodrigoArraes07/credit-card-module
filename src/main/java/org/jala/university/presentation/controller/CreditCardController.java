// CreditCardController.java
package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.service.CreditCardService;

import java.util.UUID;

public class CreditCardController {
    private final CreditCardService creditCardService;

    @FXML private TextField cardIdField;
    @FXML private TextField userIdField;
    @FXML private Label messageLabel;
    @FXML private Button blockButton;
    @FXML private Button unblockButton;
    @FXML private Button cancelButton;

    public CreditCardController(CreditCardService service) {
        this.creditCardService = service;
    }

    @FXML
    public void initialize() {
        blockButton.setOnAction(e -> blockCard());
        unblockButton.setOnAction(e -> unblockCard());
        cancelButton.setOnAction(e -> cancelCard());
    }

    private void blockCard() {
        try {
            UUID cardId = UUID.fromString(cardIdField.getText());
            UUID userId = UUID.fromString(userIdField.getText());

            CreditCardDto dto = creditCardService.blockCardWithMessage(cardId, userId);
            messageLabel.setText(dto.getMessage());
        } catch (IllegalArgumentException e) {
            messageLabel.setText("ID inválido.");
        }
    }

    private void unblockCard() {
        try {
            UUID cardId = UUID.fromString(cardIdField.getText());
            UUID userId = UUID.fromString(userIdField.getText());

            CreditCardDto dto = creditCardService.unblockCardWithMessage(cardId, userId);
            messageLabel.setText(dto.getMessage());
        } catch (IllegalArgumentException e) {
            messageLabel.setText("ID inválido.");
        }
    }

    private void cancelCard() {
        try {
            UUID cardId = UUID.fromString(cardIdField.getText());
            UUID userId = UUID.fromString(userIdField.getText());

            CreditCardDto dto = creditCardService.cancelCardWithMessage(cardId, userId);
            messageLabel.setText(dto.getMessage());
        } catch (IllegalArgumentException e) {
            messageLabel.setText("ID inválido.");
        }
    }
}
