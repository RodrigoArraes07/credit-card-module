package org.jala.university.presentation.components;

import javafx.scene.control.Button;
import javafx.scene.Cursor;

public class PrimaryButton extends Button {

    public PrimaryButton() {
        super();
        applyStyles();
    }

    public PrimaryButton(String text) {
        super(text);
        applyStyles();
    }

    private void applyStyles() {
        getStyleClass().add("primary-button");
        setPrefHeight(45);
        setMinWidth(120);
        setCursor(Cursor.HAND);
        setStyle(
                "-fx-background-color: #2196F3;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );

        setOnMouseEntered(e -> setStyle(
                "-fx-background-color: #1976D2;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        ));

        setOnMouseExited(e -> applyStyles());
    }
}