package org.jala.university;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jala.university.infrastructure.config.AppConfig;
import org.jala.university.presentation.controller.CreditCardRequestController;

public final class MainApp extends Application {

    private static final String APPLICATION_TITLE = "Credit Card Module - Solicitar Cartão";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Iniciando aplicação...");

            AppConfig config = AppConfig.getInstance();
            System.out.println("AppConfig criado");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/credit-card-request-view.fxml")
            );
            System.out.println("FXMLLoader criado");

            VBox root = loader.load();
            System.out.println("FXML carregado");

            CreditCardRequestController controller = loader.getController();
            controller.setCreditCardService(config.getCreditCardService());
            System.out.println("Service injetado no controller");

            Scene scene = new Scene(root, 600, 700);
            primaryStage.setScene(scene);
            primaryStage.setTitle(APPLICATION_TITLE);
            primaryStage.setOnCloseRequest(e -> config.close());
            primaryStage.show();

            System.out.println("Aplicação iniciada com sucesso!");

        } catch (Exception e) {
            System.err.println("ERRO ao iniciar aplicação:");
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        AppConfig.getInstance().close();
    }
}
