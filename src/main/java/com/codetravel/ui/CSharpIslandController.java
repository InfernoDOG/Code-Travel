package com.codetravel.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
/**
 * Контроллер для острова C#
 */
public class CSharpIslandController {
    
    @FXML private ImageView islandImage;
    @FXML private Button selectCSharpIsland;
    
    @FXML
    private ScrollPane islandScrollPane; // Имя должно строго совпадать с fx:id в FXML


    @FXML
    public void initialize() {
        // Загрузка изображения острова при инициализации
        try {
            islandImage.setImage(new javafx.scene.image.Image(
                getClass().getResourceAsStream("/texture/islands/csharp-bg.png")));
        } catch (Exception e) {
            System.err.println("Ошибка загрузки изображения острова C#: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSelectCSharpIsland(ActionEvent event) {
        System.out.println("Выбран остров C#");
        // Здесь будет логика для выбора острова C#
        // Можно добавить переход на экран выбора персонажа или напрямую на игру
    }
    
    @FXML
    private void handleBackToIslands(ActionEvent event) {
        System.out.println("Возврат к выбору островов");
        // Логика возврата к главному меню выбора островов
        loadIslandsSelectionScreen();
    }
    
    private void loadIslandsSelectionScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/islands-selection.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) selectCSharpIsland.getScene().getWindow();
            Scene scene = new Scene(root, 400, 900);
            scene.getStylesheets().add(getClass().getResource("/styles/common-dark.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/styles/islands-selection.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Выбор острова");
            stage.show();
        } catch (Exception e) {
            System.err.println("Ошибка загрузки экрана выбора островов: " + e.getMessage());
        }
    }
}
