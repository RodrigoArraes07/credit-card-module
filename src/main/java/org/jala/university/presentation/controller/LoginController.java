package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jala.university.application.dto.UserDto;
import org.jala.university.application.service.UserService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.infrastructure.config.AppConfig;
import org.jala.university.presentation.CreditCardView;
import org.jala.university.presentation.context.UserContext;

public class LoginController extends BaseController {

    @FXML
    private TextField cpfField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private UserService userService;

    public void setUserService(UserService service) {
        this.userService = service;
    }

    @FXML
    private void initialize() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        if (userService == null) {
            userService = AppConfig.getInstance().getUserService();
        }
    }

    @FXML
    private void handleLogin() {
        clearErrors();

        String cpf = cpfField.getText();
        String password = passwordField.getText();

        if (!validateFields(cpf, password)) {
            return;
        }

        try {
            if (userService == null) {
                userService = AppConfig.getInstance().getUserService();
            }

            UserDto user = userService.authenticate(cpf, password);

            if (user == null) {
                showError("CPF ou senha inválidos");
                setFieldError(cpfField, true);
                setFieldError(passwordField, true);
                return;
            }

            UserContext context = new UserContext(user.getId());
            ViewSwitcher.switchTo(CreditCardView.HOME.getView(), context);

        } catch (Exception e) {
            showError("Erro ao realizar login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRequestCard() {
        ViewSwitcher.switchTo(CreditCardView.CARD_TYPE_SELECTION.getView());
    }

    private boolean validateFields(String cpf, String password) {
        boolean valid = true;

        if (cpf == null || cpf.isBlank()) {
            showError("Preencha o CPF");
            setFieldError(cpfField, true);
            valid = false;
        }

        if (password == null || password.isBlank()) {
            showError("Preencha a senha");
            setFieldError(passwordField, true);
            valid = false;
        }

        return valid;
    }

    private void clearErrors() {
        setFieldError(cpfField, false);
        setFieldError(passwordField, false);
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private void setFieldError(TextField field, boolean hasError) {
        if (hasError) {
            field.setStyle(
                "-fx-pref-height: 50; " +
                "-fx-background-color: white; " +
                "-fx-border-color: #D32F2F; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-padding: 12; " +
                "-fx-font-size: 14px;"
            );
        } else {
            field.setStyle(
                "-fx-pref-height: 50; " +
                "-fx-background-color: #F5F5F5; " +
                "-fx-border-color: transparent; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-padding: 12; " +
                "-fx-font-size: 14px;"
            );
        }
    }

    private void setFieldError(PasswordField field, boolean hasError) {
        if (hasError) {
            field.setStyle(
                "-fx-pref-height: 50; " +
                "-fx-background-color: white; " +
                "-fx-border-color: #D32F2F; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-padding: 12; " +
                "-fx-font-size: 14px;"
            );
        } else {
            field.setStyle(
                "-fx-pref-height: 50; " +
                "-fx-background-color: #F5F5F5; " +
                "-fx-border-color: transparent; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-padding: 12; " +
                "-fx-font-size: 14px;"
            );
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
