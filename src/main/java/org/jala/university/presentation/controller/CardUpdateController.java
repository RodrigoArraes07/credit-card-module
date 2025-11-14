package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.presentation.components.LimitSlider;
import org.jala.university.presentation.components.StyledComboBox;
import org.jala.university.presentation.components.StyledPasswordField;
import org.jala.university.application.dto.CreditCardUpdateDto;
import org.jala.university.application.dto.CreditCardUpdateResponseDto;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewContext;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.infrastructure.config.AppConfig;
import org.jala.university.presentation.CreditCardView;
import org.jala.university.presentation.context.CardUpdateContext;
import org.jala.university.presentation.context.UserContext;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class CardUpdateController extends BaseController {

    @FXML
    private VBox passwordContainer;

    @FXML
    private VBox sliderContainer;

    @FXML
    private VBox comboBoxContainer;

    private StyledPasswordField passwordField;
    private LimitSlider limitSlider;
    private Label sliderValueLabel;
    private StyledComboBox<Integer> newDueDayComboBox;

    @FXML
    private Button updateButton;

    @FXML
    private Label passwordError;

    @FXML
    private Label newLimitError;

    @FXML
    private Label newDueDayError;

    @FXML
    private Label generalError;

    @FXML
    private VBox cardInfoContainer;

    @FXML
    private VBox updateFieldsContainer;

    @FXML
    private VBox successContainer;

    @FXML
    private Label currentCardLabel;

    @FXML
    private Label currentLimitLabel;

    @FXML
    private Label maxLimitLabel;

    @FXML
    private Label currentDueDayLabel;

    @FXML
    private Label successMessage;

    @FXML
    private Label limitHintLabel;

    private UUID userId;
    private String cardNumber;
    private String maskCardNumber;
    private CreditCardService creditCardService;
    private CreditCardDto currentCard;
    private NumberFormat currencyFormatter;

    @FXML
    private void initialize() {
        currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        initializeComponents();
    }

    private void initializeComponents() {
        passwordField = new StyledPasswordField("••••");
        passwordField.setMaxWidth(200);
        passwordField.setOnAction(e -> handleValidatePassword());
        passwordContainer.getChildren().add(passwordField);

        limitSlider = new LimitSlider(50, 10000, 5000);
        sliderValueLabel = new Label("5.000,00");
        sliderValueLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #1565C0; -fx-font-weight: 700;");
        sliderContainer.getChildren().addAll(sliderValueLabel, limitSlider);

        newDueDayComboBox = new StyledComboBox<>();
        newDueDayComboBox.setPromptText("Selecione o dia de vencimento");
        newDueDayComboBox.setPrefWidth(300);
        newDueDayComboBox.setMaxWidth(300);
        newDueDayComboBox.getItems().addAll(5, 10, 15, 20, 25, 30);
        comboBoxContainer.getChildren().add(newDueDayComboBox);

        setupPasswordMask();
        setupSlider();
    }

    @Override
    public void setContext(ViewContext context) {
        super.setContext(context);

        if (context instanceof CardUpdateContext cardUpdateContext) {
            this.userId = cardUpdateContext.getUserId();
            this.cardNumber = cardUpdateContext.getCardNumber();
            this.maskCardNumber = cardUpdateContext.getMaskCardNumber();

            if (creditCardService == null) {
                creditCardService = AppConfig.getInstance().getCreditCardService();
            }

            loadCardInfo();
        }
    }

    private void loadCardInfo() {
        try {
            currentCard = creditCardService.findByCardNumber(cardNumber);

            if (currentCard == null) {
                showError(generalError, "Erro ao carregar informações do cartão");
                return;
            }

            currentCardLabel.setText(maskCardNumber);
            currentLimitLabel.setText(currencyFormatter.format(currentCard.getLimitAmount()));
            maxLimitLabel.setText(currencyFormatter.format(currentCard.getMaxLimit()));

            Integer currentDueDay = getCurrentDueDay();
            currentDueDayLabel.setText(currentDueDay != null ? "Dia " + currentDueDay : "Não definido");

            limitHintLabel.setText(String.format("Máx: %s",
                currencyFormatter.format(currentCard.getMaxLimit())));

            limitSlider.setMin(50);
            limitSlider.setMax(currentCard.getMaxLimit());
            limitSlider.setValue(currentCard.getLimitAmount());
            sliderValueLabel.setText(String.format("%,.2f", currentCard.getLimitAmount()));

        } catch (Exception e) {
            showError(generalError, "Erro ao carregar informações do cartão: " + e.getMessage());
        }
    }

    private void setupPasswordMask() {
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                passwordField.setText(oldVal);
            } else if (newVal.length() > 4) {
                passwordField.setText(newVal.substring(0, 4));
            }
            hideError(passwordError);
        });
    }

    private void setupSlider() {
        limitSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double value = newVal.doubleValue();
            sliderValueLabel.setText(String.format("%,.2f", value));
            hideError(newLimitError);
        });

        sliderValueLabel.setText(String.format("%,.2f", limitSlider.getValue()));
    }

    @FXML
    private void handleValidatePassword() {
        hideAllErrors();

        String password = passwordField.getText();

        if (password.length() != 4) {
            showError(passwordError, "Senha deve ter 4 dígitos");
            return;
        }

        if (!currentCard.getPasswordHash().equals(password)) {
            showError(passwordError, "Senha incorreta");
            return;
        }

        cardInfoContainer.setVisible(true);
        cardInfoContainer.setManaged(true);
        updateFieldsContainer.setVisible(true);
        updateFieldsContainer.setManaged(true);
    }

    private Integer getCurrentDueDay() {
        return 10;
    }

    @FXML
    private void handleUpdate() {
        hideAllErrors();

        String password = passwordField.getText();
        Double sliderValue = limitSlider.getValue();
        Integer newDueDay = newDueDayComboBox.getValue();

        if (password.length() != 4) {
            showError(passwordError, "Valide a senha primeiro");
            return;
        }

        Double newLimit = null;
        boolean limitChanged = Math.abs(sliderValue - currentCard.getLimitAmount()) > 0.01;

        if (limitChanged) {
            newLimit = sliderValue;

            if (newLimit < 50) {
                showError(newLimitError, "Limite mínimo é R$ 50,00");
                return;
            }

            if (newLimit > currentCard.getMaxLimit()) {
                showError(newLimitError, String.format("Limite não pode exceder %s",
                    currencyFormatter.format(currentCard.getMaxLimit())));
                return;
            }
        }

        if (!limitChanged && newDueDay == null) {
            showError(generalError, "Altere o limite ou selecione uma nova data de vencimento");
            return;
        }

        updateButton.setDisable(true);
        updateButton.setText("Processando...");

        CreditCardUpdateDto updateDto = CreditCardUpdateDto.builder()
                .cardNumber(cardNumber)
                .password(password)
                .newLimitAmount(newLimit)
                .newDueDay(newDueDay)
                .build();

        try {
            CreditCardUpdateResponseDto response = creditCardService.updateCreditCardSettings(updateDto);

            if (response.getSuccess()) {
                displaySuccess(response);
            } else {
                showError(generalError, response.getMessage());
                updateButton.setDisable(false);
                updateButton.setText("Atualizar Informações");
            }

        } catch (Exception e) {
            showError(generalError, "Erro ao atualizar cartão: " + e.getMessage());
            updateButton.setDisable(false);
            updateButton.setText("Atualizar Informações");
        }
    }

    private void displaySuccess(CreditCardUpdateResponseDto response) {
        StringBuilder message = new StringBuilder();
        message.append("Cartão: ").append(response.getMaskCardNumber()).append("\n\n");

        if (response.getUpdatedLimit() != null) {
            message.append("Novo limite: ").append(currencyFormatter.format(response.getUpdatedLimit())).append("\n");
        }

        if (response.getUpdatedDueDay() != null) {
            message.append("Novo vencimento: Dia ").append(response.getUpdatedDueDay()).append("\n");
        }

        if (response.getAffectedInvoices() != null && response.getAffectedInvoices() > 0) {
            message.append("\nFaturas afetadas: ").append(response.getAffectedInvoices());
        }

        successMessage.setText(message.toString());

        cardInfoContainer.setVisible(false);
        cardInfoContainer.setManaged(false);
        updateFieldsContainer.setVisible(false);
        updateFieldsContainer.setManaged(false);
        successContainer.setVisible(true);
        successContainer.setManaged(true);
    }

    @FXML
    private void handleBack() {
        if (userId != null) {
            UserContext context = new UserContext(userId);
            ViewSwitcher.switchTo(CreditCardView.HOME.getView(), context);
        } else {
            ViewSwitcher.switchTo(CreditCardView.HOME.getView());
        }
    }

    @FXML
    private void handleBackToHome() {
        handleBack();
    }

    private void showError(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError(Label errorLabel) {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private void hideAllErrors() {
        hideError(passwordError);
        hideError(newLimitError);
        hideError(newDueDayError);
        hideError(generalError);
    }
}
