package org.jala.university.presentation.components;

import javafx.scene.control.Button;
import javafx.scene.Cursor;

public class SecondaryButton extends Button {

    public SecondaryButton() {
        super();
        applyStyles();
    }

    public SecondaryButton(String text) {
        super(text);
        applyStyles();
    }

    private void applyStyles() {
        getStyleClass().add("secondary-button");
        setPrefHeight(45);
        setMinWidth(120);
        setCursor(Cursor.HAND);
        setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: #2196F3;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #2196F3;" +
            "-fx-border-radius: 5;" +
            "-fx-border-width: 2;" +
            "-fx-cursor: hand;"
        );

        setOnMouseEntered(e -> setStyle(
            "-fx-background-color: #E3F2FD;" +
            "-fx-text-fill: #2196F3;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #2196F3;" +
            "-fx-border-radius: 5;" +
            "-fx-border-width: 2;" +
            "-fx-cursor: hand;"
        ));

        setOnMouseExited(e -> applyStyles());
    }
}
