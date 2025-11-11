package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.domain.enums.CardType;
import org.jala.university.infrastructure.config.AppConfig;
import org.jala.university.presentation.CreditCardView;
import org.jala.university.presentation.context.CardTypeContext;

public class CardTypeSelectionController extends BaseController {

    @FXML
    private VBox cardContainer;

    @FXML
    private void initialize() {
        buildCardOptions();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/login-view.fxml")
            );
            Parent root = loader.load();

            LoginController controller = loader.getController();
            controller.setUserService(AppConfig.getInstance().getUserService());

            Stage stage = (Stage) cardContainer.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            System.err.println("Error loading login view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void buildCardOptions() {
        cardContainer.getChildren().clear();

        for (CardType cardType : CardType.values()) {
            VBox cardOption = createCardOption(cardType);
            cardContainer.getChildren().add(cardOption);
        }
    }

    private VBox createCardOption(CardType cardType) {
        VBox container = new VBox();
        container.setSpacing(0);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(700);
        container.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 4); " +
            "-fx-cursor: hand;"
        );
        VBox.setMargin(container, new Insets(0, 0, 24, 0));

        HBox contentBox = new HBox();
        contentBox.setSpacing(32);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        contentBox.setPadding(new Insets(32));

        ImageView imageView = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream(cardType.getImagePath()));
            imageView.setImage(image);
            imageView.setFitWidth(280);
            imageView.setFitHeight(180);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Error loading card image: " + e.getMessage());
        }

        VBox textBox = new VBox();
        textBox.setSpacing(12);
        textBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label titleLabel = new Label(cardType.getDisplayName());
        titleLabel.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #1565C0;"
        );

        Label descriptionLabel = new Label(cardType.getDescription());
        descriptionLabel.setStyle(
            "-fx-font-size: 16px; " +
            "-fx-text-fill: #757575; " +
            "-fx-wrap-text: true;"
        );
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(280);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button selectButton = new Button("Select This Card");
        selectButton.setStyle(
            "-fx-background-color: #1565C0; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 12 32; " +
            "-fx-background-radius: 8; " +
            "-fx-cursor: hand;"
        );
        selectButton.setOnAction(e -> handleCardSelection(cardType));

        textBox.getChildren().addAll(titleLabel, descriptionLabel, spacer, selectButton);

        contentBox.getChildren().addAll(imageView, textBox);
        container.getChildren().add(contentBox);

        container.setOnMouseEntered(e -> {
            container.setStyle(
                container.getStyle() +
                "-fx-effect: dropshadow(gaussian, rgba(21, 101, 192, 0.3), 16, 0, 0, 6);"
            );
        });

        container.setOnMouseExited(e -> {
            container.setStyle(
                container.getStyle().replace(
                    "-fx-effect: dropshadow(gaussian, rgba(21, 101, 192, 0.3), 16, 0, 0, 6);",
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 4);"
                )
            );
        });

        container.setCursor(Cursor.HAND);

        return container;
    }

    private void handleCardSelection(CardType cardType) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/credit-card-request-view.fxml")
            );
            Parent root = loader.load();

            CreditCardRequestController controller = loader.getController();
            controller.setCreditCardService(AppConfig.getInstance().getCreditCardService());
            controller.setSelectedCardType(cardType);

            Stage stage = (Stage) cardContainer.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            System.err.println("Error loading credit card request view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
