package org.jala.university.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.infrastructure.config.AppConfig;
import org.jala.university.infrastructure.config.EnvConfig;
import org.jala.university.presentation.controller.LoginController;

public final class MainView extends Application {

    private static final String APPLICATION_TITLE = "JALA Credit Card Management";
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;

    static {
        EnvConfig.load();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Starting application...");

            AppConfig config = AppConfig.getInstance();

            Scene scene = new Scene(new Pane(), WINDOW_WIDTH, WINDOW_HEIGHT);
            ViewSwitcher.setup(primaryStage, scene);

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/login-view.fxml")
            );
            Pane root = loader.load();

            LoginController controller = loader.getController();
            controller.setUserService(config.getUserService());

            scene.setRoot(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(APPLICATION_TITLE);
            primaryStage.setOnCloseRequest(e -> config.close());
            primaryStage.show();

            System.out.println("Application started successfully!");

        } catch (Exception e) {
            System.err.println("ERROR starting application:");
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        AppConfig.getInstance().close();
    }
}
