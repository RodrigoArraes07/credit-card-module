package org.jala.university.presentation.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.jala.university.application.dto.CreditCardStatusUpdateRequestDto;
import org.jala.university.application.dto.CreditCardStatusUpdateResponseDto;
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

public class CardSecurityController extends BaseController {

    @FXML
    private Label headerTitle;

    @FXML
    private Circle iconCircle;

    @FXML
    private FontAwesomeIconView mainIcon;

    @FXML
    private Label actionTitle;

    @FXML
    private Label actionSubtitle;

    @FXML
    private Label cardNumberLabel;

    @FXML
    private HBox currentStatusBadgeContainer;

    @FXML
    private VBox warningBox;

    @FXML
    private Label warningMessage;

    @FXML
    private VBox passwordFieldContainer;

    @FXML
    private Label passwordError;

    @FXML
    private Button confirmButton;

    private StyledPasswordField passwordField;
    private UUID userId;
    private String cardNumber;
    private String maskCardNumber;
    private CreditCardStatus currentStatus;
    private CreditCardService creditCardService;
    private boolean isBlockAction;

    @FXML
    private void initialize() {
        creditCardService = AppConfig.getInstance().getCreditCardService();
        initializePasswordField();
    }

    private void initializePasswordField() {
        passwordField = new StyledPasswordField("••••");
        passwordField.setMaxWidth(Double.MAX_VALUE);
        passwordField.setOnAction(e -> handleConfirm());
        passwordFieldContainer.getChildren().add(passwordField);
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
        isBlockAction = (currentStatus == CreditCardStatus.ACTIVE);

        cardNumberLabel.setText(maskCardNumber);

        currentStatusBadgeContainer.getChildren().clear();
        StatusBadge badge = new StatusBadge(currentStatus);
        currentStatusBadgeContainer.getChildren().add(badge);

        updateUIForAction();
    }

    private void updateUIForAction() {
        if (isBlockAction) {
            headerTitle.setText("Bloquear Cartão");
            actionTitle.setText("Bloquear Cartão");
            actionTitle.setStyle("-fx-font-size: 26px; -fx-text-fill: #D32F2F; -fx-font-weight: 700;");
            actionSubtitle.setText("Bloqueie temporariamente seu cartão para maior segurança");

            iconCircle.setStyle("-fx-fill: #FFEBEE;");
            mainIcon.setIcon(FontAwesomeIcon.LOCK);
            mainIcon.setFill(javafx.scene.paint.Color.web("#D32F2F"));

            warningBox.setStyle(
                    "-fx-padding: 25; -fx-background-color: #FFEBEE; " +
                    "-fx-border-color: #D32F2F; -fx-border-width: 2; " +
                    "-fx-border-radius: 12; -fx-background-radius: 12;");
            warningMessage.setText("Ao bloquear, o cartão não poderá ser usado até ser desbloqueado.");

            confirmButton.setText("Confirmar Bloqueio");
            confirmButton.setStyle(
                    "-fx-pref-width: 220; -fx-pref-height: 50; -fx-font-size: 15px; " +
                    "-fx-background-color: #D32F2F; -fx-text-fill: white; " +
                    "-fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: 600;");

        } else {
            headerTitle.setText("Desbloquear Cartão");
            actionTitle.setText("Desbloquear Cartão");
            actionTitle.setStyle("-fx-font-size: 26px; -fx-text-fill: #2E7D32; -fx-font-weight: 700;");
            actionSubtitle.setText("Desbloqueie seu cartão para voltar a utilizá-lo normalmente");

            iconCircle.setStyle("-fx-fill: #E8F5E9;");
            mainIcon.setIcon(FontAwesomeIcon.UNLOCK);
            mainIcon.setFill(javafx.scene.paint.Color.web("#2E7D32"));

            warningBox.setStyle(
                    "-fx-padding: 25; -fx-background-color: #E8F5E9; " +
                    "-fx-border-color: #2E7D32; -fx-border-width: 2; " +
                    "-fx-border-radius: 12; -fx-background-radius: 12;");
            warningMessage.setText("Ao desbloquear, o cartão voltará a funcionar normalmente.");

            confirmButton.setText("Confirmar Desbloqueio");
            confirmButton.setStyle(
                    "-fx-pref-width: 220; -fx-pref-height: 50; -fx-font-size: 15px; " +
                    "-fx-background-color: #2E7D32; -fx-text-fill: white; " +
                    "-fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: 600;");
        }
    }

    @FXML
    private void handleConfirm() {
        clearErrors();

        String password = passwordField.getText();

        if (password == null || password.trim().isEmpty()) {
            showError("Por favor, digite sua senha.");
            return;
        }

        processStatusChange(password);
    }

    private void processStatusChange(String password) {
        try {
            CreditCardStatusUpdateRequestDto requestDto = CreditCardStatusUpdateRequestDto.builder()
                    .userId(userId)
                    .userPassword(password)
                    .cardNumber(cardNumber)
                    .build();

            CreditCardStatusUpdateResponseDto response;
            if (isBlockAction) {
                response = creditCardService.blockCard(requestDto);
            } else {
                response = creditCardService.unblockCard(requestDto);
            }

            if (response != null && response.getSuccess() != null && response.getSuccess()) {
                showSuccessAndGoBack(response.getMessage());
            } else {
                String errorMessage = response != null && response.getMessage() != null ?
                        response.getMessage() : "Erro ao processar operação.";
                showError(errorMessage);
            }

        } catch (Exception e) {
            showError("Erro ao processar operação: " + e.getMessage());
        }
    }

    private void showSuccessAndGoBack(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        handleBack();
    }

    private void showError(String message) {
        passwordError.setText(message);
        passwordError.setVisible(true);
        passwordError.setManaged(true);
        passwordField.setError(true);
    }

    private void clearErrors() {
        passwordError.setVisible(false);
        passwordError.setManaged(false);
        passwordField.setError(false);
    }

    @FXML
    private void handleBack() {
        ViewSwitcher.switchTo(CreditCardView.HOME.getView(), new UserContext(userId));
    }
}