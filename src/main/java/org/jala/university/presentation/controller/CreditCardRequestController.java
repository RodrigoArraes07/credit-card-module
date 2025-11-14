package org.jala.university.presentation.controller;

import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.jala.university.application.dto.CreditCardRequestDto;
import org.jala.university.application.dto.CreditCardResponseDto;
import org.jala.university.application.dto.UserCreateDto;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.enums.CardType;
import org.jala.university.domain.enums.CreditCardStatus;
import org.jala.university.infrastructure.config.AppConfig;
import org.jala.university.infrastructure.persistence.UserRepositoryImpl;
import org.jala.university.infrastructure.util.PasswordHashUtil;

import java.text.NumberFormat;
import java.util.Locale;

public class CreditCardRequestController extends BaseController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField cpfField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField incomeField;

    @FXML
    private PasswordField userPasswordField;

    @FXML
    private PasswordField cardPasswordField;

    @FXML
    private VBox limitCard;

    @FXML
    private Label limitValue;

    @FXML
    private VBox passwordBox;

    @FXML
    private VBox cardPasswordBox;

    @FXML
    private Button submitButton;

    @FXML
    private Label messageLabel;

    private CreditCardService creditCardService;
    private Double calculatedLimit;
    private final UserRepositoryImpl userRepository;
    private CardType selectedCardType;

    public CreditCardRequestController() {
        AppConfig appConfig = AppConfig.getInstance();
        EntityManager entityManager = appConfig.getEntityManager();
        this.userRepository = new UserRepositoryImpl(entityManager);
    }

    public void setCreditCardService(CreditCardService service) {
        this.creditCardService = service;
    }

    public void setSelectedCardType(CardType cardType) {
        this.selectedCardType = cardType;
    }

    @Override
    public void setContext(org.jala.university.commons.presentation.ViewContext context) {
        super.setContext(context);
        if (context instanceof org.jala.university.presentation.context.CardTypeContext) {
            org.jala.university.presentation.context.CardTypeContext cardTypeContext =
                (org.jala.university.presentation.context.CardTypeContext) context;
            setSelectedCardType(cardTypeContext.getCardType());
            if (creditCardService == null) {
                creditCardService = org.jala.university.infrastructure.config.AppConfig.getInstance().getCreditCardService();
            }
        }
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleBack() {
        org.jala.university.commons.presentation.ViewSwitcher.switchTo(
            org.jala.university.presentation.CreditCardView.CARD_TYPE_SELECTION.getView()
        );
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

            cardPasswordBox.setVisible(true);
            cardPasswordBox.setManaged(true);

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

        try {
            User user = userRepository.findByCpf(cpfField.getText());

            if (user == null) {
                UserCreateDto userCreateDto = UserCreateDto.builder()
                        .name(nameField.getText())
                        .email(emailField.getText())
                        .cpf(cpfField.getText())
                        .password_hash(userPasswordField.getText())
                        .build();

                user = userRepository.save(userCreateDto);

                if (user == null) {
                    showMessage("Erro ao criar usuário", "#f44336");
                    return;
                }
            }

            CreditCardRequestDto request = CreditCardRequestDto.builder()
                    .holderName(user.getName())
                    .cpf(user.getCpf())
                    .email(user.getEmail())
                    .monthlyIncome(Double.parseDouble(incomeField.getText().replace(",", ".")))
                    .password(cardPasswordField.getText())
                    .cardType(selectedCardType)
                    .build();

            CreditCardResponseDto response = creditCardService.requestCreditCard(request, user);

            if (response.getStatus() == CreditCardStatus.ACTIVE) {
                showSuccessMessage(response);

                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        javafx.application.Platform.runLater(this::navigateToLogin);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                showMessage(response.getMessage(), "#f44336");
            }
        } catch (Exception e) {
            showMessage("Erro ao processar a solicitação: " + e.getMessage(), "#f44336");
            e.printStackTrace();
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

        String userPassword = userPasswordField.getText();
        if (userPassword == null || !PasswordHashUtil.isValidPassword(userPassword)) {
            setFieldError(userPasswordField, true);
            showMessage("A senha de login deve ter no mínimo 6 caracteres, incluindo letras e números", "#f44336");
            valid = false;
        }

        if (cardPasswordField.getText() == null || cardPasswordField.getText().length() != 4) {
            setFieldError(cardPasswordField, true);
            showMessage("A senha do cartão deve ter exatamente 4 dígitos", "#f44336");
            valid = false;
        }

        return valid;
    }

    private void clearErrors() {
        setFieldError(nameField, false);
        setFieldError(cpfField, false);
        setFieldError(emailField, false);
        setFieldError(incomeField, false);
        setFieldError(userPasswordField, false);
        setFieldError(cardPasswordField, false);
    }

    private void setFieldError(TextField field, boolean hasError) {
        if (hasError) {
            field.setStyle(
                    "-fx-pref-height: 40; -fx-background-color: white; -fx-border-color: #f44336; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");
        } else {
            field.setStyle(
                    "-fx-pref-height: 40; -fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");
        }
    }

    private void setFieldError(PasswordField field, boolean hasError) {
        if (hasError) {
            field.setStyle(
                    "-fx-pref-height: 40; -fx-background-color: white; -fx-border-color: #f44336; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");
        } else {
            field.setStyle(
                    "-fx-pref-height: 40; -fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");
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
                formatCurrency(response.getApprovedLimit()));
        showMessage(successMessage, "#4CAF50");
    }

    private void clearForm() {
        nameField.clear();
        cpfField.clear();
        emailField.clear();
        incomeField.clear();
        userPasswordField.clear();
        cardPasswordField.clear();

        limitCard.setVisible(false);
        limitCard.setManaged(false);

        passwordBox.setVisible(false);
        passwordBox.setManaged(false);

        cardPasswordBox.setVisible(false);
        cardPasswordBox.setManaged(false);

        submitButton.setVisible(false);
        submitButton.setManaged(false);

        calculatedLimit = null;
    }

    private String formatCurrency(Double value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatter.format(value);
    }

    private void navigateToLogin() {
        org.jala.university.commons.presentation.ViewSwitcher.switchTo(
            org.jala.university.presentation.CreditCardView.LOGIN.getView()
        );
    }

}
