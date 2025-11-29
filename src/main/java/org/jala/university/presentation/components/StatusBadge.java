package org.jala.university.presentation.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import org.jala.university.domain.enums.CreditCardStatus;

public class StatusBadge extends HBox {

    private final CreditCardStatus status;

    public StatusBadge(CreditCardStatus status) {
        this.status = status;
        buildBadge();
    }

    private void buildBadge() {
        setAlignment(Pos.CENTER);
        setSpacing(6);
        setPadding(new Insets(6, 12, 6, 12));

        String backgroundColor;
        String textColor;
        String dotColor;
        String statusText;

        switch (status) {
            case ACTIVE -> {
                backgroundColor = "#E8F5E9";
                textColor = "#2E7D32";
                dotColor = "#4CAF50";
                statusText = "ATIVO";
            }
            case BLOCKED -> {
                backgroundColor = "#FFEBEE";
                textColor = "#D32F2F";
                dotColor = "#F44336";
                statusText = "BLOQUEADO";
            }
            case SUSPENDED -> {
                backgroundColor = "#FFF3E0";
                textColor = "#E65100";
                dotColor = "#FF9800";
                statusText = "SUSPENSO";
            }
            case EXPIRED -> {
                backgroundColor = "#ECEFF1";
                textColor = "#455A64";
                dotColor = "#78909C";
                statusText = "EXPIRADO";
            }
            default -> {
                backgroundColor = "#F5F5F5";
                textColor = "#757575";
                dotColor = "#BDBDBD";
                statusText = "DESCONHECIDO";
            }
        }

        setStyle(
                "-fx-background-color: " + backgroundColor + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " + textColor + ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 12;");

        Circle dot = new Circle(4);
        dot.setStyle("-fx-fill: " + dotColor + ";");

        Label label = new Label(statusText);
        label.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: " + textColor + ";" +
                "-fx-letter-spacing: 0.5;");

        getChildren().addAll(dot, label);
    }

    public CreditCardStatus getStatus() {
        return status;
    }
}