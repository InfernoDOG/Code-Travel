package com.codetravel.ui;

import com.codetravel.core.JavaFXGame;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AuthController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheckBox;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private Label errorLabel;
    @FXML private VBox mainContainer;
    
    private com.codetravel.database.AuthDatabase dbManager = com.codetravel.database.AuthDatabase.getInstance();
    
    @FXML
    public void initialize() {
        setupUI();
        checkRememberedUser();
    }

    private void setupUI() {
        // Твои стили контейнера
        mainContainer.setStyle(
            "-fx-background-color: #1a1a2e;" +
            "-fx-background-radius: 15px;" +
            "-fx-border-color: #FFD700;" +
            "-fx-border-width: 3px;" +
            "-fx-border-radius: 15px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 20, 0.5, 0, 0);" +
            "-fx-padding: 40px;" +
            "-fx-spacing: 20px;"
        );
        
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
        passwordField.setStyle(fieldStyle);
        
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
        
        loginButton.setStyle(buttonStyle);
        registerButton.setStyle(buttonStyle);
        
        rememberMeCheckBox.setStyle(
            "-fx-text-fill: #FFFFFF;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-font-size: 14px;"
        );
        
        forgotPasswordLink.setStyle(
            "-fx-text-fill: #FFD700;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-font-size: 12px;" +
            "-fx-underline: true;"
        );
        
        errorLabel.setStyle(
            "-fx-text-fill: #FF4444;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;"
        );
        
        usernameField.setPromptText("Имя пользователя");
        passwordField.setPromptText("Пароль");
        
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        passwordField.setOnAction(e -> handleLogin());
        usernameField.setOnAction(e -> passwordField.requestFocus());
        
        setupButtonHover(loginButton);
        setupButtonHover(registerButton);
        
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
    }
    
    private void setupButtonHover(Button button) {
        String originalStyle = button.getStyle();
        String hoverStyle = originalStyle.replace("0.8)", "1.0)").replace("#FFD700", "#FFA500");
        
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(originalStyle));
    }
    
    private void checkRememberedUser() {
        try {
            File rememberFile = new File("remember_user.txt");
            if (rememberFile.exists()) {
                Scanner scanner = new Scanner(rememberFile);
                if (scanner.hasNextLine()) {
                    String username = scanner.nextLine();
                    String password = scanner.hasNextLine() ? scanner.nextLine() : "";
                    
                    usernameField.setText(username);
                    passwordField.setText(password);
                    rememberMeCheckBox.setSelected(true);
                    
                    System.out.println("Восстановлены данные пользователя: " + username);
                }
                scanner.close();
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения данных пользователя: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Пожалуйста, заполните все поля!");
            return;
        }
        
        // Валидация через класс бд
        if (dbManager.validateLogin(username, password)) {
            System.out.println("Вход разрешен для пользователя: " + username);
            
            // Обрабатываем галочку "Запомнить меня"
            if (rememberMeCheckBox.isSelected()) {
                saveUserCredentials(username, password);
            } else {
                deleteSavedCredentials();
            }
            
            // Переход к главному меню игры
            try {
                JavaFXGame.showMainMenu();
            } catch (IOException e) {
                showError("Ошибка загрузки главного меню");
                e.printStackTrace();
            }
        } else {
            showError("Неверное имя пользователя или пароль.");
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            JavaFXGame.showRegistrationScreen();
        } catch (IOException e) {
            showError("Ошибка загрузки экрана регистрации");
        }
    }
    
    @FXML
    private void handleForgotPassword() {
        showError("Функция восстановления пароля в разработке");
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle(errorLabel.getStyle());
    }
    
    private void clearError() {
        errorLabel.setVisible(false);
    }
    
    private void saveUserCredentials(String username, String password) {
        try (FileWriter writer = new FileWriter("remember_user.txt")) {
            writer.write(username + "\n");
            writer.write(password + "\n");
            System.out.println("Данные пользователя сохранены локально.");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения данных: " + e.getMessage());
        }
    }
    
    private void deleteSavedCredentials() {
        File rememberFile = new File("remember_user.txt");
        if (rememberFile.exists()) {
            if (rememberFile.delete()) {
                System.out.println("Сохраненные данные пользователя удалены.");
            }
        }
    }
}