package org.jala.university.presentation.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jala.university.application.dto.UserDto;
import org.jala.university.application.service.UserService;
import org.jala.university.application.session.SessionManager;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.infrastructure.config.AppConfig;
import org.jala.university.presentation.CreditCardView;
import org.jala.university.presentation.context.UserContext;

import java.util.UUID;

public class LoginController extends BaseController {

    @FXML
    private TextField cpfField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private FontAwesomeIconView togglePasswordIcon;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Label errorLabel;

    private UserService userService;
    private final SessionManager sessionManager = SessionManager.getInstance();
    private boolean isPasswordVisible = false;

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

        setupCpfMask();
        setupPasswordSync();
        tryAutoLogin();
    }

    private void setupPasswordSync() {
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!isPasswordVisible) {
                passwordTextField.setText(newVal);
            }
        });

        passwordTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (isPasswordVisible) {
                passwordField.setText(newVal);
            }
        });
    }

    @FXML
    private void handleTogglePassword() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordTextField.setVisible(true);
            togglePasswordIcon.setGlyphName("EYE");
        } else {
            passwordField.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            passwordField.setVisible(true);
            togglePasswordIcon.setGlyphName("EYE_SLASH");
        }
    }

    private void setupCpfMask() {
        cpfField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            String cleaned = newValue.replaceAll("[^\\d]", "");

            if (cleaned.length() > 11) {
                cleaned = cleaned.substring(0, 11);
            }

            StringBuilder formatted = new StringBuilder();
            int length = cleaned.length();

            for (int i = 0; i < length; i++) {
                if (i == 3 || i == 6) {
                    formatted.append('.');
                } else if (i == 9) {
                    formatted.append('-');
                }
                formatted.append(cleaned.charAt(i));
            }

            String result = formatted.toString();
            if (!result.equals(newValue)) {
                cpfField.setText(result);
                cpfField.positionCaret(result.length());
            }
        });
    }

    private void tryAutoLogin() {
        if (sessionManager.hasSavedSession()) {
            UUID savedUserId = sessionManager.getSavedUserId();
            if (savedUserId != null) {
                try {
                    UserDto user = userService.findById(savedUserId);
                    if (user != null) {
                        sessionManager.setCurrentUser(user);
                        UserContext context = new UserContext(user.getId());
                        ViewSwitcher.switchTo(CreditCardView.HOME.getView(), context);
                    } else {
                        sessionManager.clearSavedSession();
                    }
                } catch (Exception e) {
                    sessionManager.clearSavedSession();
                    showError("Erro ao restaurar sessão");
                }
            }
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

            String cleanedCpf = cpf.replaceAll("[^\\d]", "");
            UserDto user = userService.authenticate(cleanedCpf, password);

            if (user == null) {
                showError("CPF ou senha inválidos");
                setFieldError(cpfField, true);
                setFieldError(passwordField, true);
                setFieldError(passwordTextField, true);
                return;
            }

            boolean rememberMe = rememberMeCheckBox.isSelected();
            sessionManager.startSession(user, rememberMe);

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
            setFieldError(passwordTextField, true);
            valid = false;
        }

        return valid;
    }

    private void clearErrors() {
        setFieldError(cpfField, false);
        setFieldError(passwordField, false);
        setFieldError(passwordTextField, false);
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
