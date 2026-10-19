package com.codetravel.ui;

import com.codetravel.core.SaveManager;
import com.codetravel.entities.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;

public class SaveDialogController {
    
    private Player player;
    private Stage dialogStage;
    
    @FXML private Label slot1InfoLabel;
    @FXML private Label slot2InfoLabel;
    @FXML private Label slot3InfoLabel;
    @FXML private Button slot1Button;
    @FXML private Button slot2Button;
    @FXML private Button slot3Button;
    
    public void setPlayer(Player player) {
        this.player = player;
        updateSlotInfo();
    }
    
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }
    
    private void updateSlotInfo() {
        updateSlot(1, slot1InfoLabel, slot1Button);
        updateSlot(2, slot2InfoLabel, slot2Button);
        updateSlot(3, slot3InfoLabel, slot3Button);
    }
    
    private void updateSlot(int slotNumber, Label infoLabel, Button button) {
        File saveFile = new File("save_slot_" + slotNumber + ".dat");
        
        if (saveFile.exists()) {
            try {
                Player existingPlayer = SaveManager.loadGame(slotNumber);
                if (existingPlayer != null) {
                    infoLabel.setText(existingPlayer.getName() + " (" + existingPlayer.getCharacterClass() + ", Lvl " + existingPlayer.getLevel() + ")");
                    button.setText("⟳");
                    button.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                } else {
                    infoLabel.setText("[ПОВРЕЖДЕН]");
                    button.setText("СОХРАНИТЬ");
                    button.setStyle("");
                }
            } catch (Exception e) {
                infoLabel.setText("[ПОВРЕЖДЕН]");
                button.setText("СОХРАНИТЬ");
                button.setStyle("");
            }
        } else {
            infoLabel.setText("[ПУСТО]");
            button.setText("СОХРАНИТЬ");
            button.setStyle("");
        }
    }
    
    @FXML
    private void handleSaveSlot1() {
        saveToSlot(1);
    }
    
    @FXML
    private void handleSaveSlot2() {
        saveToSlot(2);
    }
    
    @FXML
    private void handleSaveSlot3() {
        saveToSlot(3);
    }
    
    private void saveToSlot(int slotNumber) {
        try {
            SaveManager.saveGame(player, slotNumber);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Сохранение");
            alert.setHeaderText(null);
            alert.setContentText("Игра успешно сохранена в слот " + slotNumber + "!");
            alert.showAndWait();
            
            dialogStage.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка сохранения");
            alert.setContentText("Не удалось сохранить игру: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
