package org.jala.university.presentation.components;

import javafx.scene.control.TextField;

public class FormTextField extends TextField {

    public FormTextField() {
        super();
        applyStyles();
    }

    public FormTextField(String promptText) {
        super();
        setPromptText(promptText);
        applyStyles();
    }

    private void applyStyles() {
        getStyleClass().add("form-text-field");
        setPrefHeight(40);
        setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10;" +
            "-fx-font-size: 14px;"
        );
    }

    public void setError(boolean hasError) {
        if (hasError) {
            setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #f44336;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 10;" +
                "-fx-font-size: 14px;"
            );
        } else {
            applyStyles();
        }
    }
}
