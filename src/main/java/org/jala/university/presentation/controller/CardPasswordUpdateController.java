package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jala.university.application.dto.CreditCardPasswordUpdateRequestDto;
import org.jala.university.application.dto.CreditCardPasswordUpdateResponseDto;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewContext;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.domain.enums.CreditCardStatus;
import org.jala.university.infrastructure.config.AppConfig;
import org.jala.university.presentation.CreditCardView;
import org.jala.university.presentation.components.StatusBadge;
import org.jala.university.presentation.components.StyledPasswordField;
import org.jala.university.presentation.context.CardSecurityContext;
import org.jala.university.presentation.context.UserContext;

import java.util.UUID;

public class CardPasswordUpdateController extends BaseController {

    @FXML
    private Label cardNumberLabel;

    @FXML
    private HBox currentStatusBadgeContainer;

    @FXML
    private VBox currentPasswordFieldContainer;

    @FXML
    private VBox newPasswordFieldContainer;

    @FXML
    private VBox confirmPasswordFieldContainer;

    @FXML
    private Label currentPasswordError;

    @FXML
    private Label newPasswordError;

    @FXML
    private Label confirmPasswordError;

    @FXML
    private Button confirmButton;

    private StyledPasswordField currentPasswordField;
    private StyledPasswordField newPasswordField;
    private StyledPasswordField confirmPasswordField;
    private UUID userId;
    private String cardNumber;
    private String maskCardNumber;
    private CreditCardStatus currentStatus;
    private CreditCardService creditCardService;

    @FXML
    private void initialize() {
        creditCardService = AppConfig.getInstance().getCreditCardService();
        initializePasswordFields();
    }

    private void initializePasswordFields() {
        currentPasswordField = new StyledPasswordField("Digite sua senha de login");
        currentPasswordField.setMaxWidth(Double.MAX_VALUE);
        currentPasswordFieldContainer.getChildren().add(currentPasswordField);

        newPasswordField = new StyledPasswordField("••••");
        newPasswordField.setMaxWidth(Double.MAX_VALUE);
        newPasswordFieldContainer.getChildren().add(newPasswordField);

        confirmPasswordField = new StyledPasswordField("••••");
        confirmPasswordField.setMaxWidth(Double.MAX_VALUE);
        confirmPasswordField.setOnAction(e -> handleConfirm());
        confirmPasswordFieldContainer.getChildren().add(confirmPasswordField);
    }

    @Override
    public void setContext(ViewContext context) {
        super.setContext(context);

        if (context instanceof CardSecurityContext ctx) {
            this.userId = ctx.getUserId();
            this.cardNumber = ctx.getCardNumber();
            this.maskCardNumber = ctx.getMaskCardNumber();
            this.currentStatus = ctx.getCurrentStatus();

            loadData();
        }
    }

    private void loadData() {
        cardNumberLabel.setText(maskCardNumber);

        currentStatusBadgeContainer.getChildren().clear();
        StatusBadge badge = new StatusBadge(currentStatus);
        currentStatusBadgeContainer.getChildren().add(badge);
    }

    @FXML
    private void handleConfirm() {
        clearErrors();

        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!validateInputs(currentPassword, newPassword, confirmPassword)) {
            return;
        }

        processPasswordUpdate(currentPassword, newPassword, confirmPassword);
    }

    private boolean validateInputs(String currentPassword, String newPassword, String confirmPassword) {
        boolean isValid = true;

        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            showCurrentPasswordError("Por favor, digite sua senha de login.");
            isValid = false;
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            showNewPasswordError("Por favor, digite a nova senha.");
            isValid = false;
        } else if (!newPassword.matches("\\d{4}")) {
            showNewPasswordError("A senha deve conter exatamente 4 dígitos numéricos.");
            isValid = false;
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            showConfirmPasswordError("Por favor, confirme a nova senha.");
            isValid = false;
        } else if (!confirmPassword.equals(newPassword)) {
            showConfirmPasswordError("As senhas não coincidem.");
            isValid = false;
        }

        return isValid;
    }

    private void processPasswordUpdate(String currentPassword, String newPassword, String confirmPassword) {
        try {
            CreditCardPasswordUpdateRequestDto requestDto = CreditCardPasswordUpdateRequestDto.builder()
                    .userId(userId)
                    .userLoginPassword(currentPassword)
                    .cardNumber(cardNumber)
                    .newPassword(newPassword)
                    .confirmNewPassword(confirmPassword)
                    .build();

            CreditCardPasswordUpdateResponseDto response = creditCardService.updateCardPassword(requestDto);

            if (response != null && response.getSuccess() != null && response.getSuccess()) {
                showSuccessAndGoBack(response.getMessage());
            } else {
                String errorMessage = response != null && response.getMessage() != null ?
                        response.getMessage() : "Erro ao atualizar senha.";
                showCurrentPasswordError(errorMessage);
            }

        } catch (Exception e) {
            showCurrentPasswordError("Erro ao processar operação: " + e.getMessage());
        }
    }

    private void showSuccessAndGoBack(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        handleBack();
    }

    private void showCurrentPasswordError(String message) {
        currentPasswordError.setText(message);
        currentPasswordError.setVisible(true);
        currentPasswordError.setManaged(true);
        currentPasswordField.setError(true);
    }

    private void showNewPasswordError(String message) {
        newPasswordError.setText(message);
        newPasswordError.setVisible(true);
        newPasswordError.setManaged(true);
        newPasswordField.setError(true);
    }

    private void showConfirmPasswordError(String message) {
        confirmPasswordError.setText(message);
        confirmPasswordError.setVisible(true);
        confirmPasswordError.setManaged(true);
        confirmPasswordField.setError(true);
    }

    private void clearErrors() {
        currentPasswordError.setVisible(false);
        currentPasswordError.setManaged(false);
        currentPasswordField.setError(false);

        newPasswordError.setVisible(false);
        newPasswordError.setManaged(false);
        newPasswordField.setError(false);

        confirmPasswordError.setVisible(false);
        confirmPasswordError.setManaged(false);
        confirmPasswordField.setError(false);
    }

    @FXML
    private void handleBack() {
        ViewSwitcher.switchTo(CreditCardView.HOME.getView(), new UserContext(userId));
    }
}
