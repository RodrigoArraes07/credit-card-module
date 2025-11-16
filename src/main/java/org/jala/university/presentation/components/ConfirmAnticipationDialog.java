package org.jala.university.presentation.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class ConfirmAnticipationDialog extends Dialog<Boolean> {

    private final String invoiceAmount;
    private final String dueDate;

    public ConfirmAnticipationDialog(String invoiceAmount, String dueDate) {
        this.invoiceAmount = invoiceAmount;
        this.dueDate = dueDate;

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
        okButton.setText("Sim, Continuar");
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
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.TOP_LEFT);
        container.setMinWidth(480);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12;");

        Label title = new Label("Antecipar Pagamento");
        title.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1565C0;");

        Label subtitle = new Label("Confirmar antecipação de pagamento");
        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #757575;");

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #E0E0E0;");

        VBox detailsBox = new VBox(12);
        detailsBox.setPadding(new Insets(15));
        detailsBox.setStyle(
                "-fx-background-color: #FAFAFA;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #E0E0E0;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 8;");

        HBox amountRow = createDetailRow("Valor atual:", invoiceAmount);
        HBox dueDateRow = createDetailRow("Vencimento:", dueDate);

        detailsBox.getChildren().addAll(amountRow, dueDateRow);

        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(15));
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setStyle(
                "-fx-background-color: #E3F2FD;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #1565C0;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 8;");

        Label infoTitle = new Label("Benefício");
        infoTitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #1565C0;");

        Label infoText = new Label("Ao antecipar o pagamento, seu limite será liberado imediatamente.");
        infoText.setWrapText(true);
        infoText.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #424242;");

        infoBox.getChildren().addAll(infoTitle, infoText);

        Label confirmQuestion = new Label("Deseja continuar com a antecipação?");
        confirmQuestion.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #212121;");

        container.getChildren().addAll(
                title,
                subtitle,
                separator,
                detailsBox,
                infoBox,
                confirmQuestion
        );

        getDialogPane().setContent(container);
    }

    private HBox createDetailRow(String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelText = new Label(label);
        labelText.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #757575;" +
                "-fx-min-width: 120;");

        Label valueText = new Label(value);
        valueText.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #212121;");

        row.getChildren().addAll(labelText, valueText);
        return row;
    }

    private void setupResultConverter() {
        setResultConverter(dialogButton -> dialogButton == ButtonType.OK);
    }
}