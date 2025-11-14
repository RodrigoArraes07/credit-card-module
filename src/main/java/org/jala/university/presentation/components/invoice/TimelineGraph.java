package org.jala.university.presentation.components.invoice;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import org.jala.university.application.dto.InvoiceDetailDto;

import java.util.function.Consumer;

public class TimelineGraph extends Region {

    private final Pane drawPane;
    private final ObjectProperty<InvoiceDetailDto> previousInvoice = new SimpleObjectProperty<>();
    private final ObjectProperty<InvoiceDetailDto> currentInvoice = new SimpleObjectProperty<>();
    private final ObjectProperty<InvoiceDetailDto> nextInvoice = new SimpleObjectProperty<>();
    private final ObjectProperty<InvoiceDetailDto> selectedInvoice = new SimpleObjectProperty<>();

    private Circle selectedOuterCircle;
    private Circle selectedInnerCircle;
    private Consumer<InvoiceDetailDto> onInvoiceSelected;

    public TimelineGraph() {
        drawPane = new Pane();
        drawPane.setPrefHeight(120);
        getChildren().add(drawPane);

        widthProperty().addListener((obs, oldVal, newVal) -> redraw());
        currentInvoice.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedInvoice.set(newVal);
                redraw();
            }
        });
    }

    public void setData(InvoiceDetailDto previous, InvoiceDetailDto current, InvoiceDetailDto next) {
        this.previousInvoice.set(previous);
        this.currentInvoice.set(current);
        this.nextInvoice.set(next);
        this.selectedInvoice.set(current);
        redraw();
    }

    public void setOnInvoiceSelected(Consumer<InvoiceDetailDto> callback) {
        this.onInvoiceSelected = callback;
    }

    public InvoiceDetailDto getSelectedInvoice() {
        return selectedInvoice.get();
    }

    public ObjectProperty<InvoiceDetailDto> selectedInvoiceProperty() {
        return selectedInvoice;
    }

    private void redraw() {
        drawPane.getChildren().clear();

        InvoiceDetailDto current = currentInvoice.get();
        if (current == null) return;

        double paneWidth = getWidth();
        if (paneWidth == 0) paneWidth = 600;

        double paneHeight = drawPane.getPrefHeight();

        double x1 = paneWidth * 0.15;
        double x2 = paneWidth * 0.5;
        double x3 = paneWidth * 0.85;

        InvoiceDetailDto previous = previousInvoice.get();
        InvoiceDetailDto next = nextInvoice.get();

        double previousAmount = previous != null ? previous.getTotalAmount() : 0.0;
        double currentAmount = current.getTotalAmount();
        double nextAmount = next != null ? next.getTotalAmount() : 0.0;

        double maxAmount = Math.max(previousAmount, Math.max(currentAmount, nextAmount));
        if (maxAmount == 0) maxAmount = 100;

        double baseY = paneHeight * 0.7;
        double y1 = calculateYPosition(previousAmount, maxAmount, baseY);
        double y2 = calculateYPosition(currentAmount, maxAmount, baseY);
        double y3 = calculateYPosition(nextAmount, maxAmount, baseY);

        Path path = new Path();
        path.setStroke(Paint.valueOf("#BDBDBD"));
        path.setStrokeWidth(3);
        path.setFill(null);

        MoveTo moveTo = new MoveTo(x1, y1);

        CubicCurveTo curve1 = new CubicCurveTo();
        curve1.setControlX1(x1 + (x2 - x1) * 0.5);
        curve1.setControlY1(y1);
        curve1.setControlX2(x1 + (x2 - x1) * 0.5);
        curve1.setControlY2(y2);
        curve1.setX(x2);
        curve1.setY(y2);

        CubicCurveTo curve2 = new CubicCurveTo();
        curve2.setControlX1(x2 + (x3 - x2) * 0.5);
        curve2.setControlY1(y2);
        curve2.setControlX2(x2 + (x3 - x2) * 0.5);
        curve2.setControlY2(y3);
        curve2.setX(x3);
        curve2.setY(y3);

        path.getElements().addAll(moveTo, curve1, curve2);
        drawPane.getChildren().add(path);

        boolean previousPaid = previous != null && previous.getIsPaid();
        addInvoicePoint(x1, y1, previousPaid, 16, "#2E7D32", previous);
        addInvoicePoint(x2, y2, false, 20, "#7B1FA2", current);
        addInvoicePoint(x3, y3, false, 16, "#9E9E9E", next);

        FadeTransition fade = new FadeTransition(Duration.millis(600), drawPane);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private double calculateYPosition(double value, double maxValue, double baseY) {
        double normalized = value / maxValue;
        double range = 60;
        return baseY - (normalized * range);
    }

    private void addInvoicePoint(double x, double y, boolean isPaid, double size, String color, InvoiceDetailDto invoice) {
        boolean isSelected = invoice != null && selectedInvoice.get() != null
                && invoice.getId().equals(selectedInvoice.get().getId());

        double circleSize = isSelected ? size + 4 : size;
        double borderWidth = isSelected ? 4 : 3;

        Circle outerCircle = new Circle(x, y, circleSize + 4);
        outerCircle.setFill(Paint.valueOf("#FFFFFF"));
        outerCircle.setStroke(Paint.valueOf(color));
        outerCircle.setStrokeWidth(borderWidth);

        Circle innerCircle = new Circle(x, y, circleSize);
        innerCircle.setFill(Paint.valueOf(color));

        drawPane.getChildren().addAll(outerCircle, innerCircle);

        if (isSelected) {
            selectedOuterCircle = outerCircle;
            selectedInnerCircle = innerCircle;
            DropShadow selectedGlow = new DropShadow();
            selectedGlow.setColor(javafx.scene.paint.Color.web(color));
            selectedGlow.setRadius(20);
            selectedGlow.setSpread(0.3);
            outerCircle.setEffect(selectedGlow);
        }

        FontAwesomeIconView icon = new FontAwesomeIconView();
        if (isPaid) {
            icon.setGlyphName("CHECK");
        } else {
            icon.setGlyphName("FILE_TEXT");
        }
        icon.setFill(Paint.valueOf("#FFFFFF"));
        icon.setSize(String.valueOf(circleSize * 0.8));
        icon.setLayoutX(x - circleSize * 0.4);
        icon.setLayoutY(y + circleSize * 0.3);

        drawPane.getChildren().add(icon);

        DropShadow glow = new DropShadow();
        glow.setColor(javafx.scene.paint.Color.web(color));
        glow.setRadius(12);

        outerCircle.setOnMouseEntered(e -> {
            if (!isSelected) {
                outerCircle.setEffect(glow);
            }
            outerCircle.setCursor(Cursor.HAND);
        });

        outerCircle.setOnMouseExited(e -> {
            if (!isSelected) {
                outerCircle.setEffect(null);
            }
        });

        if (invoice != null) {
            outerCircle.setOnMouseClicked(e -> selectInvoice(invoice, outerCircle, innerCircle, color));
            innerCircle.setOnMouseClicked(e -> selectInvoice(invoice, outerCircle, innerCircle, color));
            icon.setOnMouseClicked(e -> selectInvoice(invoice, outerCircle, innerCircle, color));
        }
    }

    private void selectInvoice(InvoiceDetailDto invoice, Circle outerCircle, Circle innerCircle, String color) {
        if (selectedOuterCircle != null) {
            selectedOuterCircle.setStrokeWidth(3);
            selectedOuterCircle.setEffect(null);
            animateCircleResize(selectedOuterCircle, selectedOuterCircle.getRadius(), selectedOuterCircle.getRadius() - 4);
            animateCircleResize(selectedInnerCircle, selectedInnerCircle.getRadius(), selectedInnerCircle.getRadius() - 4);
        }

        selectedOuterCircle = outerCircle;
        selectedInnerCircle = innerCircle;
        selectedInvoice.set(invoice);

        outerCircle.setStrokeWidth(4);
        animateCircleResize(outerCircle, outerCircle.getRadius(), outerCircle.getRadius() + 4);
        animateCircleResize(innerCircle, innerCircle.getRadius(), innerCircle.getRadius() + 4);

        DropShadow selectedGlow = new DropShadow();
        selectedGlow.setColor(javafx.scene.paint.Color.web(color));
        selectedGlow.setRadius(20);
        selectedGlow.setSpread(0.3);
        outerCircle.setEffect(selectedGlow);

        if (onInvoiceSelected != null) {
            onInvoiceSelected.accept(invoice);
        }
    }

    private void animateCircleResize(Circle circle, double fromRadius, double toRadius) {
        Timeline timeline = new Timeline(
                new javafx.animation.KeyFrame(
                        Duration.millis(200),
                        new javafx.animation.KeyValue(circle.radiusProperty(), toRadius, javafx.animation.Interpolator.EASE_OUT)
                )
        );
        timeline.play();
    }
}
