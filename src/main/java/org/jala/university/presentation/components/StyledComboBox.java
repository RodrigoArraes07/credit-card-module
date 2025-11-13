package org.jala.university.presentation.components;

import javafx.scene.control.ComboBox;
import javafx.scene.Cursor;

public class StyledComboBox<T> extends ComboBox<T> {

    public StyledComboBox() {
        super();
        applyStyles();
    }

    private void applyStyles() {
        setPrefHeight(50);
        setCursor(Cursor.HAND);
        setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #E0E0E0;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 15;" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #424242;");

        focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                setStyle(
                        "-fx-background-color: white;" +
                        "-fx-border-color: #1565C0;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 15;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #424242;");
            } else {
                applyStyles();
            }
        });

        setOnMouseEntered(e -> {
            if (!isFocused()) {
                setStyle(
                        "-fx-background-color: #F5F5F5;" +
                        "-fx-border-color: #BDBDBD;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 15;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #424242;");
            }
        });

        setOnMouseExited(e -> {
            if (!isFocused()) {
                applyStyles();
            }
        });
    }
}
