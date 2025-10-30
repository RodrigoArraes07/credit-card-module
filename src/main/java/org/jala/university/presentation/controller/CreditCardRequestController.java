package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.jala.university.application.dto.CreditCardRequestDto;
import org.jala.university.application.dto.CreditCardResponseDto;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.domain.enums.CreditCardStatus;

import java.text.NumberFormat;
import java.util.Locale;

public class CreditCardRequestController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField cpfField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField incomeField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private VBox limitCard;

    @FXML
    private Label limitValue;

    @FXML
    private VBox passwordBox;

    @FXML
    private Button submitButton;

    @FXML
    private Label messageLabel;

    private CreditCardService creditCardService;
    private Double calculatedLimit;

    public CreditCardRequestController() {
    }

    public void setCreditCardService(CreditCardService service) {
        this.creditCardService = service;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleCalculateLimit() {
        clearErrors();

        String incomeText = incomeField.getText();
        if (incomeText == null || incomeText.isBlank()) {
            setFieldError(incomeField, true);
            showMessage("Por favor, informe a renda mensal", "#f44336");
            return;
        }

        try {
            Double income = Double.parseDouble(incomeText.replace(",", "."));
            if (income <= 0) {
                setFieldError(incomeField, true);
                showMessage("A renda deve ser maior que zero", "#f44336");
                return;
            }

            calculatedLimit = creditCardService.calculatePreApprovedLimit(income);

            limitValue.setText(formatCurrency(calculatedLimit));
            limitCard.setVisible(true);
            limitCard.setManaged(true);

            passwordBox.setVisible(true);
            passwordBox.setManaged(true);

            submitButton.setVisible(true);
            submitButton.setManaged(true);

            showMessage("Limite calculado com sucesso!", "#4CAF50");

        } catch (NumberFormatException e) {
            setFieldError(incomeField, true);
            showMessage("Renda inválida. Use apenas números", "#f44336");
        }
    }

    @FXML
    private void handleSubmit() {
        clearErrors();

        if (!validateFields()) {
            return;
        }

        CreditCardRequestDto request = CreditCardRequestDto.builder()
                .holderName(nameField.getText())
                .cpf(cpfField.getText())
                .email(emailField.getText())
                .monthlyIncome(Double.parseDouble(incomeField.getText().replace(",", ".")))
                .password(passwordField.getText())
                .build();

        CreditCardResponseDto response = creditCardService.requestCreditCard(request);

        if (response.getStatus() == CreditCardStatus.ACTIVE) {
            showSuccessMessage(response);
            clearForm();
        } else {
            showMessage(response.getMessage(), "#f44336");
        }
    }

    private boolean validateFields() {
        boolean valid = true;

        if (nameField.getText() == null || nameField.getText().isBlank()) {
            setFieldError(nameField, true);
            showMessage("Preencha todos os campos obrigatórios", "#f44336");
            valid = false;
        }

        if (cpfField.getText() == null || cpfField.getText().isBlank()) {
            setFieldError(cpfField, true);
            showMessage("Preencha todos os campos obrigatórios", "#f44336");
            valid = false;
        }

        if (emailField.getText() == null || emailField.getText().isBlank()) {
            setFieldError(emailField, true);
            showMessage("Preencha todos os campos obrigatórios", "#f44336");
            valid = false;
        }

        if (incomeField.getText() == null || incomeField.getText().isBlank()) {
            setFieldError(incomeField, true);
            showMessage("Preencha todos os campos obrigatórios", "#f44336");
            valid = false;
        }

        if (passwordField.getText() == null || passwordField.getText().length() != 4) {
            setFieldError(passwordField, true);
            showMessage("A senha deve ter exatamente 4 dígitos", "#f44336");
            valid = false;
        }

        return valid;
    }

    private void clearErrors() {
        setFieldError(nameField, false);
        setFieldError(cpfField, false);
        setFieldError(emailField, false);
        setFieldError(incomeField, false);
        setFieldError(passwordField, false);
    }

    private void setFieldError(TextField field, boolean hasError) {
        if (hasError) {
            field.setStyle("-fx-pref-height: 40; -fx-background-color: white; -fx-border-color: #f44336; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");
        } else {
            field.setStyle("-fx-pref-height: 40; -fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");
        }
    }

    private void setFieldError(PasswordField field, boolean hasError) {
        if (hasError) {
            field.setStyle("-fx-pref-height: 40; -fx-background-color: white; -fx-border-color: #f44336; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");
        } else {
            field.setStyle("-fx-pref-height: 40; -fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");
        }
    }

    private void showMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
    }

    private void showSuccessMessage(CreditCardResponseDto response) {
        String successMessage = String.format(
                "Cartão criado com sucesso!\nNúmero: %s\nLimite: %s",
                response.getMaskCardNumber(),
                formatCurrency(response.getApprovedLimit())
        );
        showMessage(successMessage, "#4CAF50");
    }

    private void clearForm() {
        nameField.clear();
        cpfField.clear();
        emailField.clear();
        incomeField.clear();
        passwordField.clear();

        limitCard.setVisible(false);
        limitCard.setManaged(false);

        passwordBox.setVisible(false);
        passwordBox.setManaged(false);

        submitButton.setVisible(false);
        submitButton.setManaged(false);

        calculatedLimit = null;
    }

    private String formatCurrency(Double value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatter.format(value);
    }

}
