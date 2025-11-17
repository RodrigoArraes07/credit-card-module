package org.jala.university.presentation.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.StageStyle;
import org.jala.university.domain.enums.PaymentMethod;

public class PaymentInstructionsDialog extends Dialog<Void> {

    private final PaymentMethod paymentMethod;

    public PaymentInstructionsDialog(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;

        setupDialog();
        buildContent();
    }

    private void setupDialog() {
        initStyle(StageStyle.UNDECORATED);
        setTitle("");
        setHeaderText(null);

        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        getDialogPane().setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 4);");

        Button closeButton = (Button) getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setText("Entendi");
        closeButton.setStyle(
                "-fx-background-color: #1565C0;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-padding: 12 40;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;");
    }

    private void buildContent() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.TOP_LEFT);
        container.setMinWidth(550);
        container.setMaxWidth(550);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12;");

        Label titleLabel = new Label("Instruções de Pagamento");
        titleLabel.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1565C0;");

        HBox methodBox = new HBox(10);
        methodBox.setAlignment(Pos.CENTER_LEFT);
        methodBox.setPadding(new Insets(10, 15, 10, 15));
        methodBox.setStyle(
                "-fx-background-color: #E3F2FD;" +
                "-fx-background-radius: 8;");

        Label methodLabel = new Label(paymentMethod.getDisplayName());
        methodLabel.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #1565C0;");

        Label descLabel = new Label("• " + paymentMethod.getDescription());
        descLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #616161;");

        methodBox.getChildren().addAll(methodLabel, new Label("-"), descLabel);

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #E0E0E0;");

        Label instructionsTitle = new Label("Passo a passo:");
        instructionsTitle.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #212121;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background: transparent;" +
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;");

        Label instructionsText = new Label(paymentMethod.getInstructions());
        instructionsText.setWrapText(true);
        instructionsText.setTextAlignment(TextAlignment.LEFT);
        instructionsText.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #424242;" +
                "-fx-line-spacing: 3px;");

        scrollPane.setContent(instructionsText);
        scrollPane.setMaxHeight(200);

        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(15));
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setStyle(
                "-fx-background-color: #FFF9E6;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #FFC107;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 8;");

        Label infoTitle = new Label("Importante");
        infoTitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #F57C00;");

        Label infoText = new Label("Após a confirmação do pagamento, o status da fatura será atualizado automaticamente no sistema.");
        infoText.setWrapText(true);
        infoText.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #616161;");

        infoBox.getChildren().addAll(infoTitle, infoText);

        container.getChildren().addAll(
                titleLabel,
                methodBox,
                separator,
                instructionsTitle,
                scrollPane,
                infoBox
        );

        getDialogPane().setContent(container);
    }
}