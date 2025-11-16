package org.jala.university.presentation.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.jala.university.domain.enums.PaymentMethod;

public class PaymentMethodDialog extends Dialog<PaymentMethod> {

    private final ToggleGroup paymentMethodGroup;
    private PaymentMethod selectedMethod;

    public PaymentMethodDialog() {
        this.paymentMethodGroup = new ToggleGroup();
        this.selectedMethod = PaymentMethod.PIX;

        setupDialog();
        buildContent();
        setupResultConverter();
    }

    private void setupDialog() {
        setTitle("Selecionar Método de Pagamento");
        setHeaderText("Escolha como deseja pagar sua fatura");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Confirmar");
        okButton.setStyle(
                "-fx-background-color: #1565C0;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;");

        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("Cancelar");
        cancelButton.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #1565C0;" +
                "-fx-border-color: #1565C0;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-weight: 600;" +
                "-fx-cursor: hand;");
    }

    private void buildContent() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.CENTER_LEFT);
        container.setMinWidth(450);

        Label instructionLabel = new Label("Selecione o método de pagamento:");
        instructionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #616161;");

        RadioButton pixRadio = createPaymentRadio(PaymentMethod.PIX);
        RadioButton boletoRadio = createPaymentRadio(PaymentMethod.BOLETO);
        RadioButton debitRadio = createPaymentRadio(PaymentMethod.DEBIT_CARD);
        RadioButton creditRadio = createPaymentRadio(PaymentMethod.CREDIT_CARD);

        pixRadio.setSelected(true);

        paymentMethodGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                RadioButton selected = (RadioButton) newVal;
                selectedMethod = (PaymentMethod) selected.getUserData();
            }
        });

        VBox radioContainer = new VBox(10);
        radioContainer.getChildren().addAll(pixRadio, boletoRadio, debitRadio, creditRadio);
        radioContainer.setPadding(new Insets(10, 0, 0, 0));

        Label securityLabel = new Label("Pagamento Seguro");
        securityLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #2E7D32;");

        container.getChildren().addAll(instructionLabel, radioContainer, securityLabel);

        getDialogPane().setContent(container);
    }

    private RadioButton createPaymentRadio(PaymentMethod method) {
        RadioButton radio = new RadioButton();
        radio.setToggleGroup(paymentMethodGroup);
        radio.setUserData(method);

        VBox labelBox = new VBox(4);
        Label nameLabel = new Label(method.getDisplayName());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 600; -fx-text-fill: #212121;");

        Label descLabel = new Label(method.getDescription());
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #757575;");

        labelBox.getChildren().addAll(nameLabel, descLabel);
        radio.setGraphic(labelBox);

        radio.setStyle(
                "-fx-cursor: hand;" +
                "-fx-padding: 12;" +
                "-fx-background-radius: 8;");

        radio.setOnMouseEntered(e -> {
            if (!radio.isSelected()) {
                radio.setStyle(
                        "-fx-cursor: hand;" +
                        "-fx-padding: 12;" +
                        "-fx-background-color: #F5F5F5;" +
                        "-fx-background-radius: 8;");
            }
        });

        radio.setOnMouseExited(e -> {
            if (!radio.isSelected()) {
                radio.setStyle(
                        "-fx-cursor: hand;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 8;");
            }
        });

        radio.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                radio.setStyle(
                        "-fx-cursor: hand;" +
                        "-fx-padding: 12;" +
                        "-fx-background-color: #E3F2FD;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #1565C0;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;");
            } else {
                radio.setStyle(
                        "-fx-cursor: hand;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 8;");
            }
        });

        return radio;
    }

    private void setupResultConverter() {
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return selectedMethod;
            }
            return null;
        });
    }
}