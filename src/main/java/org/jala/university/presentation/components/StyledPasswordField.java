package org.jala.university.presentation.components;

import javafx.scene.control.PasswordField;

public class StyledPasswordField extends PasswordField {

    public StyledPasswordField() {
        super();
        applyStyles();
    }

    public StyledPasswordField(String promptText) {
        super();
        setPromptText(promptText);
        applyStyles();
    }

    private void applyStyles() {
        setPrefHeight(50);
        setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #E0E0E0;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 12 15;" +
                "-fx-font-size: 16px;" +
                "-fx-alignment: center;" +
                "-fx-text-fill: #212121;");

        focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                setStyle(
                        "-fx-background-color: white;" +
                        "-fx-border-color: #1565C0;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12 15;" +
                        "-fx-font-size: 16px;" +
                        "-fx-alignment: center;" +
                        "-fx-text-fill: #212121;");
            } else {
                applyStyles();
            }
        });
    }

    public void setError(boolean hasError) {
        if (hasError) {
            setStyle(
                    "-fx-background-color: #FFEBEE;" +
                    "-fx-border-color: #D32F2F;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 12 15;" +
                    "-fx-font-size: 16px;" +
                    "-fx-alignment: center;" +
                    "-fx-text-fill: #212121;");
        } else {
            applyStyles();
        }
    }
}
