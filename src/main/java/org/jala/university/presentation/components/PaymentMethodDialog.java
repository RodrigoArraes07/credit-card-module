package org.jala.university.presentation.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
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
        initStyle(StageStyle.UNDECORATED);
        setTitle("");
        setHeaderText(null);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 4);");

        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Confirmar");
        okButton.setStyle(
                "-fx-background-color: #1565C0;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-padding: 12 32;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;");

        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("Cancelar");
        cancelButton.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #616161;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-padding: 12 32;" +
                "-fx-border-color: #E0E0E0;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;");
    }

    private void buildContent() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30, 30, 20, 30));
        container.setAlignment(Pos.TOP_LEFT);
        container.setMinWidth(500);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12;");

        Label title = new Label("Selecionar Método de Pagamento");
        title.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1565C0;");

        Label subtitle = new Label("Escolha como deseja pagar sua fatura");
        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #757575;");

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

        VBox radioContainer = new VBox(12);
        radioContainer.getChildren().addAll(pixRadio, boletoRadio, debitRadio, creditRadio);
        radioContainer.setPadding(new Insets(8, 0, 0, 0));

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #E0E0E0;");

        Label securityLabel = new Label("Pagamento seguro e criptografado");
        securityLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #2E7D32;");

        container.getChildren().addAll(title, subtitle, radioContainer, separator, securityLabel);

        getDialogPane().setContent(container);
    }

    private RadioButton createPaymentRadio(PaymentMethod method) {
        RadioButton radio = new RadioButton();
        radio.setToggleGroup(paymentMethodGroup);
        radio.setUserData(method);

        VBox labelBox = new VBox(5);
        labelBox.setPadding(new Insets(0, 0, 0, 10));

        Label nameLabel = new Label(method.getDisplayName());
        nameLabel.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #212121;");

        Label descLabel = new Label(method.getDescription());
        descLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #757575;");

        labelBox.getChildren().addAll(nameLabel, descLabel);
        radio.setGraphic(labelBox);

        radio.setStyle(
                "-fx-cursor: hand;" +
                "-fx-padding: 14;" +
                "-fx-background-color: #FAFAFA;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #E0E0E0;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 8;");

        radio.setOnMouseEntered(e -> {
            if (!radio.isSelected()) {
                radio.setStyle(
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14;" +
                        "-fx-background-color: #F5F5F5;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #1565C0;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 8;");
            }
        });

        radio.setOnMouseExited(e -> {
            if (!radio.isSelected()) {
                radio.setStyle(
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14;" +
                        "-fx-background-color: #FAFAFA;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 8;");
            }
        });

        radio.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                radio.setStyle(
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14;" +
                        "-fx-background-color: #E3F2FD;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #1565C0;" +
                        "-fx-border-width: 2.5;" +
                        "-fx-border-radius: 8;");
            } else {
                radio.setStyle(
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14;" +
                        "-fx-background-color: #FAFAFA;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 8;");
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