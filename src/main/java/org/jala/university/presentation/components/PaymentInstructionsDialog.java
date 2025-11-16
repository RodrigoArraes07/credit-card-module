package org.jala.university.presentation.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.jala.university.domain.enums.PaymentMethod;

public class PaymentInstructionsDialog extends Dialog<Void> {

    private final PaymentMethod paymentMethod;

    public PaymentInstructionsDialog(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;

        setupDialog();
        buildContent();
    }

    private void setupDialog() {
        String title = "Instruções de Pagamento - " + paymentMethod.getDisplayName();
        setTitle(title);
        setHeaderText(null);

        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Button closeButton = (Button) getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setText("Fechar");
        closeButton.setStyle(
                "-fx-background-color: #1565C0;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 20;" +
                "-fx-cursor: hand;");
    }

    private void buildContent() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_LEFT);
        container.setMinWidth(500);
        container.setMaxWidth(500);

        Label titleLabel = new Label(paymentMethod.getDisplayName());
        titleLabel.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1565C0;");

        Label descriptionLabel = new Label(paymentMethod.getDescription());
        descriptionLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #616161;");

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #E0E0E0;");

        Label instructionsTitle = new Label("Como pagar:");
        instructionsTitle.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #212121;");

        Label instructionsText = new Label(paymentMethod.getInstructions());
        instructionsText.setWrapText(true);
        instructionsText.setTextAlignment(TextAlignment.LEFT);
        instructionsText.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #424242;" +
                "-fx-line-spacing: 4px;");

        VBox infoBox = new VBox(8);
        infoBox.setPadding(new Insets(15));
        infoBox.setStyle(
                "-fx-background-color: #F5F5F5;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #E0E0E0;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;");

        Label infoIcon = new Label("ℹ");
        infoIcon.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-text-fill: #1565C0;");

        Label infoText = new Label("Após a confirmação do pagamento, o status da fatura será atualizado automaticamente.");
        infoText.setWrapText(true);
        infoText.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #616161;");

        infoBox.getChildren().addAll(infoIcon, infoText);

        container.getChildren().addAll(
                titleLabel,
                descriptionLabel,
                separator,
                instructionsTitle,
                instructionsText,
                infoBox
        );

        getDialogPane().setContent(container);
    }
}