package com.codetravel.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.codetravel.core.JavaFXGame;
import com.codetravel.entities.Player;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CharacterCreationController implements Initializable {
    
    @FXML private Label titleLabel;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> classComboBox;
    @FXML private TextArea classDescriptionArea;
    @FXML private ProgressBar strengthBar;
    @FXML private ProgressBar intelligenceBar;
    @FXML private ProgressBar agilityBar;
    @FXML private Label strengthValue;
    @FXML private Label intelligenceValue;
    @FXML private Label agilityValue;
    @FXML private Button backButton;
    @FXML private Button createButton;
    @FXML private VBox statsContainer;
    @FXML private ImageView classImageView;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
        setupClassComboBox();
        setupEventHandlers();
    }
    
    private void setupUI() {
        // Применяем стили для терминального вида
        if (titleLabel != null) {
            titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #00ff00; -fx-font-family: 'Consolas', monospace; -fx-effect: dropshadow(gaussian, #00ff00, 10, 0.5, 0, 0);");
        }
        
        if (classDescriptionArea != null) {
            classDescriptionArea.setStyle(
                "-fx-text-fill: #00ff00; " +
                "-fx-font-size: 12px; " +
                "-fx-font-family: 'Consolas', monospace; " +
                "-fx-background-color: #000814; " +
                "-fx-background-radius: 8px; " +
                "-fx-padding: 10px; " +
                "-fx-border-color: #00ff00; " +
                "-fx-border-width: 1px; " +
                "-fx-wrap-text: true; " +
                "-fx-hbar-policy: never; " +
                "-fx-vbar-policy: never;"
            );
        }
        
        if (statsContainer != null) {
            statsContainer.setStyle("-fx-background-color: rgba(0, 255, 0, 0.1); -fx-background-radius: 8px; -fx-padding: 15px;");
        }
        
        // Настраиваем цветные прогресс-бары для характеристик
        setupColoredStatBars();
    }
    
    private void setupColoredStatBars() {
        double defaultProgress = 0.5; // 50% прогресс по умолчанию
        
        if (strengthBar != null) {
            strengthBar.setProgress(defaultProgress);
            strengthBar.setStyle("-fx-accent: #e74c3c; -fx-background-radius: 5px;");
        }
        
        if (intelligenceBar != null) {
            intelligenceBar.setProgress(defaultProgress);
            intelligenceBar.setStyle("-fx-accent: #3498db; -fx-background-radius: 5px;");
        }
        
        if (agilityBar != null) {
            agilityBar.setProgress(defaultProgress);
            agilityBar.setStyle("-fx-accent: #f39c12; -fx-background-radius: 5px;");
        }
    }
    
    private void setupClassComboBox() {
        if (classComboBox != null) {
            classComboBox.getItems().addAll(
                "Воин кода",
                "Маг алгоритмов", 
                "Жрец данных",
                "Разработчик"
            );
            
            classComboBox.setOnAction(event -> updateClassDescription());
        }
    }
    
    private void setupEventHandlers() {
        if (createButton != null) {
            createButton.setOnAction(event -> handleCreateCharacter());
        }
        
        if (backButton != null) {
            backButton.setOnAction(event -> handleBack());
        }
        
        // Ограничение на 12 символов для имени персонажа
        if (nameField != null) {
            nameField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > 12) {
                    nameField.setText(oldValue);
                }
            });
        }
    }
    
    private void updateClassDescription() {
        String selectedClass = classComboBox.getValue();
        if (selectedClass == null) return;
        
        String description = switch (selectedClass) {
            case "Воин кода" -> "Мастер синтаксиса и структур. Специализируется на базовых конструкциях и логике. Высокая сила делает его идеальным бойцом в ближнем бою.";
            case "Маг алгоритмов" -> "Гений сложных вычислений. Владеет рекурсией и оптимизацией. Превосходный интеллект даёт преимущество в сложных задачах.";
            case "Жрец данных" -> "Хранитель информации. Работает с базами данных и структурами. Отличная ловкость помогает избегать ошибок.";
            case "Разработчик" -> "Универсальный программист. Сбалансированные навыки во всех областях. Хороший выбор для начинающих.";
            default -> "Выберите класс для просмотра описания";
        };
        
        classDescriptionArea.setText(description);
        updateStatsForClass(selectedClass);
        updateClassImage(selectedClass);
    }
    
    private void updateClassImage(String className) {
        if (classImageView == null) return;
        
        try {
            String imagePath = switch (className) {
                case "Воин кода" -> "/texture/Character/Knight/knight.png";
                case "Маг алгоритмов" -> "/texture/Character/Mage/mage.png";
                case "Жрец данных" -> "/texture/Character/Prist/data-priest.png";
                case "Разработчик" -> "/texture/Character/Developer/Developer.png";
                default -> "/texture/Character/Knight/knight.png";
            };
            
            Image classImage = new Image(getClass().getResourceAsStream(imagePath));
            classImageView.setImage(classImage);
            classImageView.setFitWidth(200);
            classImageView.setFitHeight(200);
            classImageView.setPreserveRatio(true);
            
        } catch (Exception e) {
            System.out.println("Ошибка загрузки изображения класса: " + e.getMessage());
            // Используем изображение по умолчанию
            try {
                Image defaultImage = new Image(getClass().getResourceAsStream("/texture/Character/Knight/knight.png"));
                classImageView.setImage(defaultImage);
                classImageView.setFitWidth(200);
                classImageView.setFitHeight(200);
                classImageView.setPreserveRatio(true);
            } catch (Exception ex) {
                System.out.println("Ошибка загрузки изображения по умолчанию: " + ex.getMessage());
            }
        }
    }
    
    private void updateStatsForClass(String className) {
        if (className == null) return;
        
        switch (className) {
            case "Воин кода":
                updateStats(15, 8, 12);
                break;
            case "Маг алгоритмов":
                updateStats(8, 18, 10);
                break;
            case "Жрец данных":
                updateStats(10, 12, 15);
                break;
            case "Разработчик":
                updateStats(12, 12, 12);
                break;
            default:
                updateStats(10, 10, 10);
        }
    }
    
    private void updateStats(int strength, int intelligence, int agility) {
        double maxStat = 20.0;
        
        if (strengthBar != null && strengthValue != null) {
            strengthBar.setProgress(strength / maxStat);
            strengthValue.setText(String.valueOf(strength));
        }
        
        if (intelligenceBar != null && intelligenceValue != null) {
            intelligenceBar.setProgress(intelligence / maxStat);
            intelligenceValue.setText(String.valueOf(intelligence));
        }
        
        if (agilityBar != null && agilityValue != null) {
            agilityBar.setProgress(agility / maxStat);
            agilityValue.setText(String.valueOf(agility));
        }
    }
    
    // Карта соответствия классов и островов
    private static final Map<String, String> CLASS_TO_ISLAND = new HashMap<>();
    static {
        CLASS_TO_ISLAND.put("Воин кода", "java");
        CLASS_TO_ISLAND.put("Маг алгоритмов", "java");
        CLASS_TO_ISLAND.put("Жрец данных", "java");
        CLASS_TO_ISLAND.put("Разработчик", "java");
        CLASS_TO_ISLAND.put("Воин кода", "python");
        CLASS_TO_ISLAND.put("Маг алгоритмов", "python");
        CLASS_TO_ISLAND.put("Жрец данных", "python");
        CLASS_TO_ISLAND.put("Разработчик", "python");
    }
    
    @FXML
    private void handleCreateCharacter() {
        String name = nameField.getText().trim();
        String characterClass = classComboBox.getValue();
        
        if (name.isEmpty()) {
            showAlert("Ошибка", "Введите имя персонажа!");
            return;
        }
        
        if (characterClass == null) {
            showAlert("Ошибка", "Выберите класс персонажа!");
            return;
        }
        
        try {
            Player player = new Player(name, characterClass);
            
            showAlert("Успех!", "Персонаж создан!");
            
            // Передаём созданного игрока в SelectIslandController
            com.codetravel.ui.SelectIslandController.setCurrentPlayer(player);
            
            // После создания персонажа открываем экран выбора острова
            JavaFXGame.showSelectIsland();
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось создать персонажа: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            JavaFXGame.showMainMenu();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось вернуться в главное меню: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
