package org.jala.university.presentation.components;

import javafx.scene.control.Slider;

public class LimitSlider extends Slider {

    public LimitSlider() {
        super();
        applyStyles();
    }

    public LimitSlider(double min, double max, double value) {
        super(min, max, value);
        applyStyles();
    }

    private void applyStyles() {
        setPrefWidth(500);
        setMajorTickUnit(1000);
        setMinorTickCount(4);
        setShowTickLabels(false);
        setShowTickMarks(true);
        setSnapToTicks(false);

        setStyle(
                "-fx-control-inner-background: #E3F2FD;" +
                "-fx-tick-label-fill: #616161;" +
                "-fx-background-color: transparent;");

        String sliderTrackStyle =
                ".slider .track {" +
                "    -fx-background-color: linear-gradient(to right, #E0E0E0 0%, #E0E0E0 100%);" +
                "    -fx-background-insets: 0;" +
                "    -fx-background-radius: 4;" +
                "    -fx-padding: 0.2em;" +
                "}" +
                ".slider .thumb {" +
                "    -fx-background-color: white;" +
                "    -fx-background-insets: 0;" +
                "    -fx-background-radius: 10;" +
                "    -fx-border-color: #1565C0;" +
                "    -fx-border-width: 3;" +
                "    -fx-border-radius: 10;" +
                "    -fx-effect: dropshadow(gaussian, rgba(21, 101, 192, 0.4), 6, 0, 0, 2);" +
                "    -fx-padding: 10;" +
                "}" +
                ".slider .thumb:hover {" +
                "    -fx-border-color: #0D47A1;" +
                "    -fx-effect: dropshadow(gaussian, rgba(21, 101, 192, 0.6), 8, 0, 0, 3);" +
                "}" +
                ".slider .filled-track {" +
                "    -fx-background-color: linear-gradient(to right, #1565C0 0%, #1E88E5 100%);" +
                "    -fx-background-insets: 0;" +
                "    -fx-background-radius: 4;" +
                "}";

        getStylesheets().add("data:text/css;base64," +
                java.util.Base64.getEncoder().encodeToString(sliderTrackStyle.getBytes()));
    }
}
