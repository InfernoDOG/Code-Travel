package com.codetravel.ui;

import com.codetravel.core.JavaFXGame;
import com.codetravel.entities.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;

public class SelectIslandController {
    
    private static Player currentPlayer;
    
    @FXML private Button javaIslandButton;
    @FXML private Button pythonIslandButton;
    @FXML private Button csharpIslandButton;
    @FXML private Button backButton;
    @FXML private ImageView islandImage;
    
    public static void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }
    
    @FXML
    public void initialize() {
        // Устанавливаем изображение по умолчанию (Java остров)
        loadDefaultIslandImage();
    }
    
    private void loadDefaultIslandImage() {
        try {
            // Загружаем изображение Java острова по умолчанию
            Image defaultImage = new Image(getClass().getResourceAsStream("/texture/islands/java-bg.png"));
            islandImage.setImage(defaultImage);
            System.out.println("SelectIsland: Изображение острова по умолчанию загружено");
        } catch (Exception e) {
            System.out.println("SelectIsland: Ошибка загрузки изображения по умолчанию - " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSelectJava() throws IOException {
        System.out.println("SelectIsland: Выбран Java остров");
        // Устанавливаем изображение Java острова
        loadIslandImage("/texture/islands/java-bg.png");
        // Используем созданного персонажа или создаём нового по умолчанию
        Player player = currentPlayer != null ? currentPlayer : new Player("Игрок", "Воин кода");
        JavaFXGame.showJavaGameScene(player);
    }
    
    @FXML
    private void handleSelectPython() throws IOException {
        System.out.println("SelectIsland: Выбран Python остров");
        // Устанавливаем изображение Python острова
        loadIslandImage("/texture/islands/python-bg.png");
        // Используем созданного персонажа или создаём нового по умолчанию
        Player player = currentPlayer != null ? currentPlayer : new Player("Игрок", "Маг алгоритмов");
        JavaFXGame.showPythonGameScene(player);
    }
    
    @FXML
    private void handleSelectCSharp() throws IOException {
        System.out.println("SelectIsland: Выбран C# остров");
        // Устанавливаем изображение C# острова
        loadIslandImage("/texture/islands/csharp-bg.png");
        // Используем созданного персонажа или создаём нового по умолчанию
        Player player = currentPlayer != null ? currentPlayer : new Player("Игрок", "Разработчик");
        JavaFXGame.showCSharpGameScene(player);
    }
    
    private void loadIslandImage(String imagePath) {
        try {
            Image islandImg = new Image(getClass().getResourceAsStream(imagePath));
            islandImage.setImage(islandImg);
        } catch (Exception e) {
            System.out.println("SelectIsland: Ошибка загрузки изображения острова - " + e.getMessage());
            // В случае ошибки оставляем текущее изображение
        }
    }
    
    @FXML
    private void handleBack() throws IOException {
        System.out.println("SelectIsland: Возврат в главное меню");
        JavaFXGame.showMainMenu();
    }
}
