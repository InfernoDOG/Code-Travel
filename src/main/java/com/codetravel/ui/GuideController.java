package com.codetravel.ui;

import com.codetravel.core.JavaFXGame;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class GuideController {
    @FXML private VBox contentBox;
    @FXML private Button backButton;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageIndicator;
    
    private int currentPage = 0;
    private static final int TOTAL_PAGES = 6;
    
    // Данные для каждой страницы
    private final String[] pageTitles = {
        "1. НАЧАЛО ИГРЫ",
        "2. ГЛАВНОЕ МЕНЮ", 
        "3. СОЗДАНИЕ ПЕРСОНАЖА",
        "4. БОЕВАЯ СИСТЕМА",
        "5. МИР ИГРЫ",
        "6. СОВЕТЫ"
    };
    
    private final String[] pageContents = {
        "• Запустите игру\n" +
        "• Введите логин: admin\n" +
        "• Введите пароль: 123\n" +
        "• Нажмите 'ВОЙТИ'\n" +
        "• Или создайте новый аккаунт\n" +
        "  через 'РЕГИСТРАЦИЯ'",
        
        "• НОВАЯ ИГРА - начать\n" +
        "  приключение\n" +
        "• ЗАГРУЗИТЬ ИГРУ - загрузить\n" +
        "  сохранение\n" +
        "• РУКОВОДСТВО - помощь\n" +
        "• ВЫХОД - закрыть игру",
        
        "• Выберите класс:\n" +
        "  - Воин: высокий HP,\n" +
        "    сильные атаки\n" +
        "  - Маг: мощные заклинания\n" +
        "  - Лучник: точные выстрелы\n" +
        "• Введите имя персонажа\n" +
        "• Нажмите 'СОЗДАТЬ'",
        
        "• Отвечайте на вопросы\n" +
        "  по программированию\n" +
        "• Правильный ответ =\n" +
        "  атака врага\n" +
        "• Неправильный = урон\n" +
        "  по вам\n" +
        "• Используйте подсказки\n" +
        "• Победите врага",
        
        "• Исследуйте локации\n" +
        "• Собирайте предметы\n" +
        "• Выполняйте задания\n" +
        "• Развивайте персонажа\n" +
        "• Открывайте новые области",
        
        "• Сохраняйте игру часто\n" +
        "• Изучайте основы Java\n" +
        "• Используйте подсказки\n" +
        "  мудро\n" +
        "• Развивайте все навыки\n" +
        "• Экспериментируйте с тактиками"
    };
    
    @FXML
    public void initialize() {
        setupGuideContent();
        setupUI();
        updatePage();
    }
    
    private void setupGuideContent() {
        // Заголовок
        Label titleLabel = new Label("ПО ИГРЕ");
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #FFD700;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.9), 12, 0.6, 0, 0);" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: CENTER;"
        );
        
        // Контейнер для страницы
        VBox pageContainer = new VBox(15);
        pageContainer.setStyle(
            "-fx-background-color: rgba(26, 26, 46, 0.85);" +
            "-fx-background-radius: 15px;" +
            "-fx-border-color: #FFD700;" +
            "-fx-border-width: 3px;" +
            "-fx-border-radius: 15px;" +
            "-fx-padding: 20px;" +
            "-fx-max-width: 340px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.7), 20, 0.8, 0, 0);"
        );
        
        // Динамический контент страницы
        VBox pageContent = new VBox(12);
        pageContent.setId("pageContent");
        
        pageContainer.getChildren().add(pageContent);
        contentBox.getChildren().addAll(titleLabel, pageContainer);
    }
    
    private void updatePage() {
        VBox pageContent = (VBox) contentBox.lookup("#pageContent");
        pageContent.getChildren().clear();
        
        // Заголовок текущей страницы
        Label pageTitle = new Label(pageTitles[currentPage]);
        pageTitle.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #FFA500;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: CENTER;"
        );
        
        // Контент текущей страницы
        Label pageText = new Label(pageContents[currentPage]);
        pageText.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-wrap-text: true;" +
            "-fx-max-width: 320px;" +
            "-fx-text-alignment: LEFT;" +
            "-fx-line-spacing: 2px;"
        );
        
        pageContent.getChildren().addAll(pageTitle, pageText);
        
        // Обновляем индикатор страницы
        pageIndicator.setText((currentPage + 1) + "/" + TOTAL_PAGES);
        
        // Обновляем состояние кнопок навигации
        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable(currentPage == TOTAL_PAGES - 1);
    }
    
    private void setupUI() {
        // Стиль кнопки "Назад в главное меню"
        backButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-background-color: linear-gradient(to bottom, #8B4513, #654321);" +
            "-fx-background-radius: 12px;" +
            "-fx-border-color: #FFD700;" +
            "-fx-border-width: 3px;" +
            "-fx-border-radius: 12px;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.9), 10, 0.6, 0, 0);" +
            "-fx-padding: 10px 20px;" +
            "-fx-alignment: CENTER;"
        );
        
        // Стиль кнопок навигации
        String navButtonStyle = 
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-background-color: linear-gradient(to bottom, #4169E1, #1E90FF);" +
            "-fx-background-radius: 10px;" +
            "-fx-border-color: #87CEEB;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 10px;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 8, 0.5, 0, 0);" +
            "-fx-padding: 8px 16px;";
        
        prevButton.setStyle(navButtonStyle);
        nextButton.setStyle(navButtonStyle);
        
        // Стиль индикатора страницы
        pageIndicator.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #FFD700;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 6, 0.4, 0, 0);"
        );
        
        // Hover эффекты
        setupButtonHoverEffects(backButton);
        setupButtonHoverEffects(prevButton);
        setupButtonHoverEffects(nextButton);
    }
    
    private void setupButtonHoverEffects(Button button) {
        String originalStyle = button.getStyle();
        String hoverStyle = originalStyle.replace("0.8)", "1.0)").replace("0.9)", "1.0)")
                .replace("#FFD700", "#FFA500").replace("#4169E1", "#6495ED").replace("#1E90FF", "#87CEEB");
        
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(originalStyle));
    }
    
    @FXML
    private void handleBack() throws IOException {
        JavaFXGame.showMainMenu();
    }
    
    @FXML
    private void handlePrevPage() {
        if (currentPage > 0) {
            currentPage--;
            updatePage();
        }
    }
    
    @FXML
    private void handleNextPage() {
        if (currentPage < TOTAL_PAGES - 1) {
            currentPage++;
            updatePage();
        }
    }
}
