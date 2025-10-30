package org.jala.university.presentation.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InfoCard extends VBox {

    private Label titleLabel;
    private Label valueLabel;

    public InfoCard() {
        super();
        initialize();
    }

    public InfoCard(String title, String value) {
        super();
        initialize();
        setTitle(title);
        setValue(value);
    }

    private void initialize() {
        titleLabel = new Label();
        titleLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-weight: normal;"
        );

        valueLabel = new Label();
        valueLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-text-fill: #2196F3;" +
            "-fx-font-weight: bold;"
        );

        getChildren().addAll(titleLabel, valueLabel);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setSpacing(10);
        setStyle(
            "-fx-background-color: #f5f5f5;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;"
        );
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }

    public void setValueColor(String color) {
        valueLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-weight: bold;"
        );
    }
}
