package com.codetravel.ui;

import com.codetravel.core.JavaFXGame;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;

public class MainMenuController {
    @FXML private Button newGameButton;
    @FXML private Button loadGameButton;
    @FXML private Button guideButton;
    @FXML private Button exitButton;
    @FXML private ImageView backgroundImage;
    @FXML private ImageView logoImage;
    @FXML private ImageView buttonFrameImage;

    @FXML
    public void initialize() {
        // Загружаем текстуры для фона и логотипа
        loadTextures();
        
        // Настраиваем единый стиль для всех кнопок
        setupUniformButtonStyle();
    }
    
    private void loadTextures() {
        try {
            // ФОН - растягиваем на все окно
            Image bgImage = new Image(getClass().getResourceAsStream("/texture/main-menu/fon.jpg"));
            backgroundImage.setImage(bgImage);
            
            // ЛОГОТИП - фиксированный размер
            Image logoImg = new Image(getClass().getResourceAsStream("/texture/main-menu/main-menu-logo.png"));
            logoImage.setImage(logoImg);
            
            // ФОН ДЛЯ КНОПОК - frame-b.png
            Image buttonFrameImg = new Image(getClass().getResourceAsStream("/texture/main-menu/frame-b.png"));
            buttonFrameImage.setImage(buttonFrameImg);
            
            System.out.println("MainMenu: Фон, логотип и рамка кнопок успешно загружены");
            
        } catch (Exception e) {
            System.out.println("MainMenu: Ошибка загрузки текстур - " + e.getMessage());
            // Устанавливаем резервные стили если текстуры не загрузились
            setupFallbackStyles();
        }
    }
    
    private void setupUniformButtonStyle() {
        // Единый стиль для всех кнопок - коричневый градиент с золотой рамкой
        String buttonStyle = 
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-font-family: 'Courier New', monospace; " +
            "-fx-background-color: linear-gradient(to bottom, #8B4513, #654321); " +
            "-fx-background-radius: 8px; " +
            "-fx-border-color: #FFD700; " +
            "-fx-border-width: 3px; " +
            "-fx-border-radius: 8px; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 8, 0.5, 0, 0); " +
            "-fx-padding: 12px 24px;";
        
        // Применяем единый стиль ко всем кнопкам
        newGameButton.setStyle(buttonStyle);
        loadGameButton.setStyle(buttonStyle);
        guideButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);
        
        // Добавляем hover эффекты
        setupHoverEffects(buttonStyle);
    }
    
    private void setupHoverEffects(String baseStyle) {
        // Hover эффект - усиление свечения
        String hoverStyle = baseStyle.replace("0.8)", "1.0)").replace("#FFD700", "#FFA500");
        
        // Применяем hover эффекты ко всем кнопкам
        newGameButton.setOnMouseEntered(e -> newGameButton.setStyle(hoverStyle));
        newGameButton.setOnMouseExited(e -> newGameButton.setStyle(baseStyle));
        
        loadGameButton.setOnMouseEntered(e -> loadGameButton.setStyle(hoverStyle));
        loadGameButton.setOnMouseExited(e -> loadGameButton.setStyle(baseStyle));
        
        guideButton.setOnMouseEntered(e -> guideButton.setStyle(hoverStyle));
        guideButton.setOnMouseExited(e -> guideButton.setStyle(baseStyle));
        
        exitButton.setOnMouseEntered(e -> exitButton.setStyle(hoverStyle));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(baseStyle));
    }
    
    private void setupFallbackStyles() {
        // Резервные стили если текстуры не загрузились
        backgroundImage.setStyle("-fx-background-color: #2C1810;");
        
        String fallbackStyle = "-fx-font-size: 16px; -fx-font-weight: bold; " +
                           "-fx-background-color: #8B4513; -fx-text-fill: white; " +
                           "-fx-background-radius: 8px; -fx-border-color: #FFD700; " +
                           "-fx-border-width: 2px; -fx-padding: 12px 24px;";
        
        newGameButton.setStyle(fallbackStyle);
        loadGameButton.setStyle(fallbackStyle);
        guideButton.setStyle(fallbackStyle);
        exitButton.setStyle(fallbackStyle);
    }

    @FXML
    private void handleNewGame() throws IOException {
        JavaFXGame.showCharacterCreation();
    }

    @FXML
    private void handleLoadGame() throws IOException {
        // Открываем экран выбора слотов
        JavaFXGame.showSlotSelection();
    }

    @FXML
    private void handleGuide() throws IOException {
        JavaFXGame.showGuideScreen();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
