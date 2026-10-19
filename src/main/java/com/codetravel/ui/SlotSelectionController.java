package com.codetravel.ui;

import com.codetravel.core.JavaFXGame;
import com.codetravel.core.SaveManager;
import com.codetravel.entities.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;

import java.io.File;

public class SlotSelectionController {
    
    @FXML private Label slot1NameLabel;
    @FXML private Label slot1ClassLabel;
    @FXML private Label slot1LevelLabel;
    @FXML private Button slot1Button;
    
    @FXML private Label slot2NameLabel;
    @FXML private Label slot2ClassLabel;
    @FXML private Label slot2LevelLabel;
    @FXML private Button slot2Button;
    
    @FXML private Label slot3NameLabel;
    @FXML private Label slot3ClassLabel;
    @FXML private Label slot3LevelLabel;
    @FXML private Button slot3Button;
    
    public void initialize() {
        updateSlotInfo();
    }
    
    private void updateSlotInfo() {
        updateSlot(1, slot1NameLabel, slot1ClassLabel, slot1LevelLabel, slot1Button);
        updateSlot(2, slot2NameLabel, slot2ClassLabel, slot2LevelLabel, slot2Button);
        updateSlot(3, slot3NameLabel, slot3ClassLabel, slot3LevelLabel, slot3Button);
    }
    
    private void updateSlot(int slotNumber, Label nameLabel, Label classLabel, Label levelLabel, Button button) {
        File saveFile = new File("save_slot_" + slotNumber + ".dat");
        
        if (saveFile.exists()) {
            try {
                Player player = SaveManager.loadGame(slotNumber);
                nameLabel.setText(player.getName());
                classLabel.setText(player.getCharacterClass());
                levelLabel.setText("Уровень " + player.getLevel());
                button.setText("📥");
                button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
            } catch (Exception e) {
                nameLabel.setText("[ПОВРЕЖДЕН]");
                classLabel.setText("");
                levelLabel.setText("");
                button.setText("СОЗДАТЬ");
                button.setStyle("");
            }
        } else {
            nameLabel.setText("[ПУСТО]");
            classLabel.setText("");
            levelLabel.setText("");
            button.setText("СОЗДАТЬ");
            button.setStyle("");
        }
    }
    
    @FXML
    private void handleSlot1() {
        handleSlotAction(1);
    }
    
    @FXML
    private void handleSlot2() {
        handleSlotAction(2);
    }
    
    @FXML
    private void handleSlot3() {
        handleSlotAction(3);
    }
    
    private void handleSlotAction(int slotNumber) {
        File saveFile = new File("save_slot_" + slotNumber + ".dat");
        
        if (saveFile.exists()) {
            // Загрузка существующего персонажа
            try {
                Player player = SaveManager.loadGame(slotNumber);
                if (player != null) {
                    JavaFXGame.startGame(player);
                } else {
                    showError("Ошибка загрузки", "Не удалось загрузить сохранение");
                }
            } catch (Exception e) {
                showError("Ошибка загрузки", "Файл сохранения поврежден");
            }
        } else {
            // Создание нового персонажа
            try {
                JavaFXGame.showCharacterCreation();
            } catch (Exception e) {
                showError("Ошибка", "Не удалось открыть создание персонажа");
            }
        }
    }
    
    @FXML
    private void handleBackToMenu() {
        try {
            JavaFXGame.showMainMenu();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Ошибка", "Не удалось вернуться в главное меню");
        }
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
