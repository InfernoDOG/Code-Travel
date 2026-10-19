package com.codetravel.ui;

import com.codetravel.database.AuthDatabase;
import com.codetravel.core.JavaFXGame;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class RegistrationController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button registerButton;
    @FXML private Button backButton;
    @FXML private Label errorLabel;
    @FXML private VBox mainContainer;
    
    private AuthDatabase authDatabase;
    
    @FXML
    public void initialize() {
        authDatabase = AuthDatabase.getInstance();
        setupUI();
    }
    
    private void setupUI() {
        // Стиль как в главном меню
        mainContainer.setStyle(
            "-fx-background-color: #1a1a2e;" +
            "-fx-background-radius: 15px;" +
            "-fx-border-color: #FFD700;" +
            "-fx-border-width: 3px;" +
            "-fx-border-radius: 15px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 20, 0.5, 0, 0);" +
            "-fx-padding: 20px;" +
            "-fx-spacing: 15px;"
        );
        
        // Стиль полей
        String fieldStyle = 
            "-fx-background-color: #2d2d2d;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-border-color: #FFD700;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 12px;";
        
        usernameField.setStyle(fieldStyle);
        emailField.setStyle(fieldStyle);
        passwordField.setStyle(fieldStyle);
        confirmPasswordField.setStyle(fieldStyle);
        
        // Стиль кнопок
        String buttonStyle = 
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-background-color: linear-gradient(to bottom, #8B4513, #654321);" +
            "-fx-background-radius: 8px;" +
            "-fx-border-color: #FFD700;" +
            "-fx-border-width: 3px;" +
            "-fx-border-radius: 8px;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 8, 0.5, 0, 0);" +
            "-fx-padding: 12px 24px;";
        
        registerButton.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);
        
        // Prompt текст
        usernameField.setPromptText("Придумайте имя пользователя");
        emailField.setPromptText("your@email.com");
        passwordField.setPromptText("Минимум 6 символов");
        confirmPasswordField.setPromptText("Повторите пароль");
        
        // Обработчики событий
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        // Enter для перехода между полями
        usernameField.setOnAction(e -> emailField.requestFocus());
        emailField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> confirmPasswordField.requestFocus());
        confirmPasswordField.setOnAction(e -> handleRegister());
        
        // Hover эффекты
        setupButtonHover(registerButton);
        setupButtonHover(backButton);
        
        // Очистка ошибки при вводе
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        emailField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
    }
    
    private void setupButtonHover(Button button) {
        String originalStyle = button.getStyle();
        String hoverStyle = originalStyle.replace("0.8)", "1.0)").replace("#FFD700", "#FFA500");
        
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(originalStyle));
    }
    
    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Валидация
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Пожалуйста, заполните все поля");
            return;
        }
        
        if (username.length() < 3) {
            showError("Имя пользователя должно содержать минимум 3 символа");
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            showError("Введите корректный email адрес");
            return;
        }
        
        if (password.length() < 6) {
            showError("Пароль должен содержать минимум 6 символов");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Пароли не совпадают");
            return;
        }
        
        // Регистрация
        if (authDatabase.registerUser(username, email, password)) {
            try {
                JavaFXGame.showAuthScreen();
            } catch (IOException e) {
                showError("Ошибка перехода к экрану входа");
            }
        } else {
            showError("Пользователь с таким именем уже существует");
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            JavaFXGame.showAuthScreen();
        } catch (IOException e) {
            showError("Ошибка возврата к экрану входа");
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle(
            "-fx-text-fill: #FF4444;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: rgba(255, 68, 68, 0.1);" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 10px;" +
            "-fx-border-color: #FF4444;" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 5px;"
        );
    }
    
    private void clearError() {
        errorLabel.setVisible(false);
    }
}
