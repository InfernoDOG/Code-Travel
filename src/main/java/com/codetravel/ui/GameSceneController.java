package com.codetravel.ui;

import com.codetravel.core.JavaFXGame;
import com.codetravel.entities.Player;
import com.codetravel.entities.Mob;
import com.codetravel.entities.IslandType;
import com.codetravel.levels.Island;
import com.codetravel.levels.WorldManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;

public class GameSceneController {
    @FXML private VBox mainContainer;
    @FXML private Label islandNameLabel;
    @FXML private Label islandDescriptionLabel;
    @FXML private Label playerNameLabel;
    @FXML private Label playerClassLabel;
    @FXML private Label playerLevelLabel;
    @FXML private Label playerHpLabel;
    @FXML private Label experienceLabel;
    @FXML private ProgressBar playerHealthBar;
    @FXML private ProgressBar experienceBar;
    @FXML private ScrollPane mobScrollPane;
    @FXML private GridPane mobGrid;
    @FXML private Button shopButton;
    @FXML private ImageView playerAvatarImageView;
    @FXML private ImageView classLogoImageView;

    private Player player;
    private WorldManager world;
    private IslandType currentIsland;

    @FXML
    public void initialize() {
    mobScrollPane.setOnScroll(event -> {
        if (event.getDeltaY() != 0) {
            // Перенаправляем вертикальное движение колесика (DeltaY) в горизонтальный скролл
            double speedFactor = 0.005; // Подбери скорость под себя
            mobScrollPane.setHvalue(mobScrollPane.getHvalue() - event.getDeltaY() * speedFactor);
            event.consume(); // Поглощаем событие, чтобы оно не шло дальше
        }
    });
        // Этот метод вызывается автоматически при загрузке FXML
        setupUI();
    }

    /**
     * Инициализирует контроллер с данными игрока и миром
     */
    public void initializeData(Player player, WorldManager world) {
        this.player = player;
        this.world = world;
        
        // Определяем тип острова по индексу игрока
        int islandIndex = player.getCurrentIslandIndex();
        this.currentIsland = IslandType.fromIndex(islandIndex);
        
        System.out.println("[DEBUG] GameSceneController initialized for island: " + currentIsland.getDisplayName());
        
        updatePlayerInfo();
        loadCurrentIsland();
    }

    /**
     * Инициализирует тему острова (вызывается из JavaFXGame)
     */
    public void initIsland(IslandType island) {
        this.currentIsland = island;
        System.out.println("[DEBUG] Switch theme style to island: " + island.getDisplayName());
    }

    private void setupUI() {
        if (mainContainer != null) {
            mainContainer.setStyle("-fx-background-color: #1a1a2e; -fx-padding: 20px;");
        }
        
        // Динамические цвета на основе типа острова
        String themeColor = getThemeColor();
        
        if (islandNameLabel != null) {
            islandNameLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + themeColor + ";");
        }
        if (islandDescriptionLabel != null) {
            islandDescriptionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #bbb;");
        }
        
        // Настройка кнопки магазина
        if (shopButton != null) {
            shopButton.setOnAction(e -> {
                try {
                    JavaFXGame.showShop(player, world);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
        
        // Настройка панели персонажа
        setupCharacterPanel();
        
        // 🔥 НАСТРОЙКА СКРОЛЛА ПОД СТИЛЬ И ЛЕНТУ
        if (mobScrollPane != null) {
            // Добавляем класс стиля, чтобы подтянуть кастомный скроллбар из CSS
            if (!mobScrollPane.getStyleClass().contains("custom-scroll")) {
                mobScrollPane.getStyleClass().add("custom-scroll");
            }
            
            // Убираем инлайн фон, оставляем только динамическую рамку цвета острова
            mobScrollPane.setStyle("-fx-border-color: " + themeColor + "; -fx-border-width: 1px; -fx-border-radius: 5px;");
            
            mobScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Только горизонтальный
            mobScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);     // Жестко отключаем вертикальный
            mobScrollPane.setFitToHeight(true); // КРИТИЧНО: растягивает контент по высоте, убирая баги прокрутки
        }
    }
    
    /**
     * Получает цвет темы для текущего острова
     */
    private String getThemeColor() {
        if (currentIsland == null) {
            return "#eee"; // По умолчанию для Java
        }
        switch (currentIsland) {
            case PYTHON:
                return "#2ECC40"; // Зелёный
            case CSHARP:
                return "#9C27B0"; // Фиолетовый
            default:
                return "#eee"; // Серый для Java
        }
    }
    
    /**
     * Получает цвет для кнопок боя
     */
    private String getFightButtonColor() {
        if (currentIsland == null) {
            return "#e74c3c"; // Красный по умолчанию
        }
        switch (currentIsland) {
            case PYTHON:
                return "#2ECC40"; // Зелёный
            case CSHARP:
                return "#9C27B0"; // Фиолетовый
            default:
                return "#e74c3c"; // Красный для Java
        }
    }
    
    private void setupCharacterPanel() {
        String themeColor = getThemeColor();
        
        if (playerNameLabel != null) {
            playerNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-font-family: 'Consolas', monospace;");
        }
        if (playerClassLabel != null) {
            playerClassLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + themeColor + "; -fx-font-family: 'Consolas', monospace; -fx-font-style: italic;");
        }
        if (playerLevelLabel != null) {
            playerLevelLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + themeColor + "; -fx-font-family: 'Consolas', monospace; -fx-effect: dropshadow(gaussian, " + themeColor + ", 5, 0.5, 0, 0);");
        }
        if (playerHpLabel != null) {
            playerHpLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-font-family: 'Consolas', monospace;");
        }
        if (experienceLabel != null) {
            experienceLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-font-family: 'Consolas', monospace;");
        }
        
        // Загружаем аватар игрока
        loadPlayerAvatar();
        
        if (playerHealthBar != null) {
            playerHealthBar.setStyle("-fx-accent: #e74c3c; -fx-control-inner-background: #2d3748; -fx-background-radius: 10px;");
        }
        if (experienceBar != null) {
            experienceBar.setStyle("-fx-accent: " + themeColor + "; -fx-control-inner-background: #2d3748; -fx-background-radius: 10px;");
        }
    }
    
    private void loadPlayerAvatar() {
        if (playerAvatarImageView == null || player == null) return;
        
        try {
            String playerClass = player.getCharacterClass();
            
            // Если класс не установлен, используем класс по умолчанию для текущего острова
            if (playerClass == null) {
                switch (currentIsland) {
                    case PYTHON:
                        playerClass = "Маг алгоритмов";
                        break;
                    case CSHARP:
                        playerClass = "Разработчик";
                        break;
                    default:
                        playerClass = "Воин кода";
                        break;
                }
                player.setCharacterClass(playerClass);
                System.out.println("[DEBUG] Default class set: " + playerClass);
            }
            
            // Загружаем аватар игрока
            String avatarImagePath = switch (playerClass) {
                case "Воин кода" -> "/texture/Character/Knight/knight.png";
                case "Маг алгоритмов" -> "/texture/Character/Mage/mage.png";
                case "Жрец данных" -> "/texture/Character/Prist/data-priest.png";
                case "Разработчик" -> "/texture/Character/Developer/Developer.png";
                default -> "/texture/Character/Knight/knight.png";
            };
            
            Image avatarImage = new Image(getClass().getResourceAsStream(avatarImagePath));
            playerAvatarImageView.setImage(avatarImage);
            playerAvatarImageView.setFitWidth(60);
            playerAvatarImageView.setFitHeight(60);
            playerAvatarImageView.setPreserveRatio(true);
            
            // Загружаем логотип класса
            String logoImagePath = switch (playerClass) {
                case "Воин кода" -> "/texture/Character/Knight/knight-logo.png";
                case "Маг алгоритмов" -> "/texture/Character/Mage/mage-logo.png";
                case "Жрец данных" -> "/texture/Character/Prist/data-priest-logo.png";
                case "Разработчик" -> "/texture/Character/Developer/developer-logo.png";
                default -> "/texture/Character/Knight/knight-logo.png";
            };
            
            Image logoImage = new Image(getClass().getResourceAsStream(logoImagePath));
            classLogoImageView.setImage(logoImage);
            classLogoImageView.setFitWidth(30);
            classLogoImageView.setFitHeight(30);
            classLogoImageView.setPreserveRatio(true);
            
        } catch (Exception e) {
            System.out.println("Error loading player avatar: " + e.getMessage());
            // Используем изображение по умолчанию в зависимости от острова
            try {
                String defaultAvatarPath = switch (currentIsland) {
                    case PYTHON -> "/texture/Character/Mage/mage.png";
                    case CSHARP -> "/texture/Character/Developer/Developer.png";
                    default -> "/texture/Character/Knight/knight.png";
                };
                
                Image defaultAvatar = new Image(getClass().getResourceAsStream(defaultAvatarPath));
                playerAvatarImageView.setImage(defaultAvatar);
                playerAvatarImageView.setFitWidth(60);
                playerAvatarImageView.setFitHeight(60);
                playerAvatarImageView.setPreserveRatio(true);
                
                String defaultLogoPath = switch (currentIsland) {
                    case PYTHON -> "/texture/Character/Mage/mage-logo.png";
                    case CSHARP -> "/texture/Character/Developer/developer-logo.png";
                    default -> "/texture/Character/Knight/knight-logo.png";
                };
                
                Image defaultLogo = new Image(getClass().getResourceAsStream(defaultLogoPath));
                classLogoImageView.setImage(defaultLogo);
                classLogoImageView.setFitWidth(30);
                classLogoImageView.setFitHeight(30);
                classLogoImageView.setPreserveRatio(true);
            } catch (Exception ex) {
                System.out.println("Error loading default avatar: " + ex.getMessage());
            }
        }
    }

    private void updatePlayerInfo() {
        if (playerNameLabel != null) {
            playerNameLabel.setText(player.getName());
        }
        if (playerClassLabel != null) {
            playerClassLabel.setText(player.getCharacterClass());
        }
        if (playerLevelLabel != null) {
            playerLevelLabel.setText(String.valueOf(player.getLevel()));
        }
        if (playerHpLabel != null) {
            playerHpLabel.setText(player.getHealth() + "/" + player.getMaxHealth());
        }
        if (experienceLabel != null) {
            int currentExp = player.getExperience();
            int expForNextLevel = player.getLevel() * 100;
            experienceLabel.setText(currentExp + "/" + expForNextLevel);
        }
        if (playerHealthBar != null) {
            playerHealthBar.setProgress((double) player.getHealth() / player.getMaxHealth());
        }
        if (experienceBar != null) {
            int currentExp = player.getExperience();
            int expForNextLevel = player.getLevel() * 100;
            experienceBar.setProgress((double) currentExp / expForNextLevel);
        }
    }

    private void loadCurrentIsland() {
        Island island = world.getCurrentIsland();
        if (island == null) {
            return;
        }
        
        if (islandNameLabel != null) {
            islandNameLabel.setText(island.getName());
        }
        if (islandDescriptionLabel != null) {
            islandDescriptionLabel.setText(island.getDescription());
        }
        
        // Создаем карточки мобов в сетке ленты
        if (mobGrid != null) {
            mobGrid.getChildren().clear();
            
            // Настраиваем сетку под идеальный горизонтальный ряд
            mobGrid.setHgap(15); // Расстояние между карточками по горизонтали
            mobGrid.setVgap(0);  // Вертикальный отступ больше не нужен (строка всегда одна)
            mobGrid.setAlignment(javafx.geometry.Pos.CENTER_LEFT); // Выравнивание по левому краю для аккуратного скролла
            
            if (island.getMobs() != null) {
                int column = 0;
                
                // Исправлено: Добавляем всех мобов строго в одну линию (row всегда равен 0)
                for (Mob mob : island.getMobs()) {
                    createMobCard(mob, column, 0);
                    column++;
                }
            }
        }
    }

    private void createMobCard(Mob mob, int column, int row) {
        String themeColor = getThemeColor();
        String fightButtonColor = getFightButtonColor();
        
        VBox card = new VBox(12); // Увеличенный отступ для воздуха
        card.setAlignment(javafx.geometry.Pos.CENTER);
        
        boolean isBoss = mob.getType() == Mob.Type.BOSS || mob.getType() == Mob.Type.MINI_BOSS;
        
        // БАЗОВЫЕ СТИЛИ (Градиенты и полупрозрачность для бесшовной интеграции в интерфейс)
        String baseCardStyle = 
            "-fx-background-color: linear-gradient(to bottom right, rgba(44, 62, 80, 0.85), rgba(26, 26, 46, 0.95)); " +
            "-fx-padding: 14px; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-radius: 12px; ";
            
        String normalBorder = "-fx-border-color: linear-gradient(to bottom, " + themeColor + ", rgba(255,255,255,0.1)); -fx-border-width: 1px; ";
        String bossBorder = "-fx-border-color: linear-gradient(to bottom, " + themeColor + ", #f39c12); -fx-border-width: 2px; ";
        
        // Мягкие тени
        String normalShadow = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 4);";
        String bossShadow = "-fx-effect: dropshadow(three-pass-box, " + themeColor + ", 15, 0.2, 0, 0);";

        // Размеры карточек, чтобы длинные имена не зажимались по бокам
        if (isBoss) {
            card.setStyle(baseCardStyle + bossBorder + bossShadow);
            card.setPrefSize(180, 195);
        } else {
            card.setStyle(baseCardStyle + normalBorder + normalShadow);
            card.setPrefSize(150, 195);
        }
        
        // 🔥 ЭФФЕКТ НАВЕДЕНИЯ (HOVER) — карточка плавно оживает и светится цветом острова
        card.setOnMouseEntered(e -> {
            card.setScaleX(1.03); 
            card.setScaleY(1.03);
            card.setStyle(baseCardStyle + (isBoss ? bossBorder : normalBorder) + "-fx-effect: dropshadow(three-pass-box, " + themeColor + ", 18, 0.4, 0, 0);");
            card.setCursor(javafx.scene.Cursor.HAND);
        });
        
        card.setOnMouseExited(e -> {
            card.setScaleX(1.0); 
            card.setScaleY(1.0);
            card.setStyle(baseCardStyle + (isBoss ? bossBorder + bossShadow : normalBorder + normalShadow));
        });
        
        GridPane.setHgrow(card, javafx.scene.layout.Priority.ALWAYS);
        GridPane.setVgrow(card, javafx.scene.layout.Priority.ALWAYS);

        // Иконка моба
        Label iconLabel = new Label(getMobIcon(mob.getType()));
        iconLabel.setStyle("-fx-font-size: 28px; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.5), 5, 0, 0, 2);");
        iconLabel.setAlignment(javafx.geometry.Pos.CENTER);
        iconLabel.setMaxWidth(Double.MAX_VALUE);

        // Имя моба
        Label nameLabel = new Label(mob.getName());
        nameLabel.setWrapText(true);
        nameLabel.setTextOverrun(javafx.scene.control.OverrunStyle.CLIP);
        nameLabel.setAlignment(javafx.geometry.Pos.CENTER);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        
        if (isBoss) {
            nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: linear-gradient(to right, #fff, " + themeColor + "); -fx-font-family: 'Consolas', monospace;");
        } else {
            nameLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #ecf0f1; -fx-font-family: 'Consolas', monospace;");
        }
        
        // HP текст
        Label hpText = new Label("HP: " + mob.getHealth());
        hpText.setStyle("-fx-font-size: 11px; -fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-family: 'Consolas', monospace;");
        hpText.setAlignment(javafx.geometry.Pos.CENTER);
        hpText.setMaxWidth(Double.MAX_VALUE);
        
        // Кнопка боя
        Button fightButton = new Button("БОЙ");
        String btnBaseStyle = "-fx-background-color: " + fightButtonColor + "; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-font-family: 'Consolas', monospace;";
        fightButton.setStyle(btnBaseStyle);
        fightButton.setAlignment(javafx.geometry.Pos.CENTER);
        fightButton.setMaxWidth(Double.MAX_VALUE);
        
        // Эффект наведения на кнопку боя
        fightButton.setOnMouseEntered(e -> fightButton.setStyle(btnBaseStyle + "-fx-background-color: derive(" + fightButtonColor + ", 20%); -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 5, 0, 0, 2);"));
        fightButton.setOnMouseExited(e -> fightButton.setStyle(btnBaseStyle));
        
        VBox spring = new VBox();
        VBox.setVgrow(spring, javafx.scene.layout.Priority.ALWAYS);
        
        fightButton.setOnAction(e -> {
            System.out.println("=== НАЧАЛО РЕТРО-БОЯ ===");
            try {
                JavaFXGame.showRetroCombat(player, mob, world);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        card.getChildren().addAll(iconLabel, nameLabel, spring, hpText, fightButton);
        
        GridPane.setConstraints(card, column, row);
        mobGrid.getChildren().add(card);
    }
    
    private String getMobIcon(Mob.Type mobType) {
        if (mobType == Mob.Type.BOSS) return "👑";
        if (mobType == Mob.Type.MINI_BOSS) return "👹";
        return "👾"; 
    }

    public void refreshUI() {
        updatePlayerInfo();
        loadCurrentIsland();
    }

    @FXML
    private void handleBackToMenu() throws IOException {
        JavaFXGame.showMainMenu();
    }
    
    @FXML
    private void handleSaveGame() {
        try {
            showSaveDialog();
        } catch (Exception e) {
            System.err.println("Error opening save dialog: " + e.getMessage());
            e.printStackTrace();
            
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
            );
            alert.setTitle("Ошибка сохранения");
            alert.setHeaderText("Не удалось открыть диалог сохранения");
            alert.setContentText("Произошла ошибка: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    private void showSaveDialog() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/fxml/save-dialog.fxml")
            );
            javafx.scene.Parent root = loader.load();
            
            SaveDialogController controller = loader.getController();
            controller.setPlayer(player);
            
            javafx.stage.Stage dialogStage = new javafx.stage.Stage();
            dialogStage.setTitle("Сохранение игры");
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.initOwner(
                ((javafx.scene.Node) playerHealthBar).getScene().getWindow()
            );
            
            controller.setDialogStage(dialogStage);
            
            javafx.scene.Scene scene = new javafx.scene.Scene(root, 400, 450);
            scene.getStylesheets().add(
                getClass().getResource("/styles/common-dark.css").toExternalForm()
            );
            scene.getStylesheets().add(
                getClass().getResource("/styles/slot-selection.css").toExternalForm()
            );
            
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to load save dialog", e);
        }
    }
}