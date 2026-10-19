package com.codetravel.core;

import com.codetravel.combat.CSharpCombatManager;
import com.codetravel.combat.CombatManager;
import com.codetravel.combat.PythonCombatManager;
import com.codetravel.entities.Player;
import com.codetravel.entities.Mob;
import com.codetravel.entities.IslandType;
import com.codetravel.levels.Island;
import com.codetravel.levels.WorldManager;
import com.codetravel.ui.GameSceneController;
import com.codetravel.ui.RetroCombatController;
import com.codetravel.ui.ShopController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class JavaFXGame extends Application {
    private static JavaFXGame instance;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        primaryStage = stage;
        primaryStage.setTitle("Code-Travel: RPG of Languages");
        primaryStage.setResizable(false);
        
        showAuthScreen();
        primaryStage.show();
        com.codetravel.database.AuthDatabase.getInstance();
    }

    public static void showMainMenu() throws IOException {
        instance.showMainMenuInstance();
    }

    public static void showCharacterCreation() throws IOException {
        instance.showCharacterCreationInstance();
    }
    
    public static void startGame(Player player) throws IOException {
        instance.startGameInstance(player);
    }
    
    public static void startPythonGame(Player player) throws IOException {
        instance.startGameInstance(player);
    }
    
    public static void startCSharpGame(Player player) throws IOException {
        instance.startGameInstance(player);
    }
    
    public static void showSlotSelection() throws IOException {
        instance.showSlotSelectionInstance();
    }
    
    public static void showRetroCombat(Player player, Mob enemy, WorldManager world) throws IOException {
        instance.showRetroCombatInstance(player, enemy, world);
    }
    
    public static void showAuthScreen() throws IOException {
        instance.showAuthScreenInstance();
    }
    
    public static void showRegistrationScreen() throws IOException {
        instance.showRegistrationScreenInstance();
    }
    
    public static void showShop(Player player, WorldManager world) throws IOException {
        instance.showShopInstance(player, world);
    }

    public static void showGameScene(Player player, WorldManager world) throws IOException {
        instance.showGameSceneInstance(player, world);
    }

    public static void showIslandSpecificGameScene(Player player, WorldManager world) throws IOException {
        instance.showIslandSpecificGameSceneInstance(player, world);
    }

    public static void backToGameScene(Player player, WorldManager world) throws IOException {
        instance.showIslandSpecificGameSceneInstance(player, world);
    }

    private void showMainMenuInstance() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-menu.fxml")); // или другое имя файла
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 400, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/common-dark.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showCharacterCreationInstance() throws IOException {
        FXMLLoader loader = new FXMLLoader(JavaFXGame.class.getResource("/fxml/character-creation.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 400, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/common-dark.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/character-creation.css").toExternalForm());
        primaryStage.setScene(scene);
    }
    
    private void startGameInstance(Player player) throws IOException {
        // 1. Сначала проверяем и корректируем индекс острова у игрока
        System.out.println("DEBUG: Player current island index from save: " + player.getCurrentIslandIndex());
        
        if (player.getCurrentIslandIndex() < 0) {
            System.out.println("DEBUG: Setting new player island index to 0 (Java island)");
            player.setCurrentIslandIndex(0);
        } else {
            System.out.println("DEBUG: Keeping existing player island index: " + player.getCurrentIslandIndex());
        }

        // 2. ИСПРАВЛЕНИЕ: Передаем реальный сохраненный индекс острова в конструктор WorldManager!
        WorldManager world = new WorldManager(player.getCurrentIslandIndex());
        world.generateIslands();
        
        if (world.getCurrentIsland() != null) {
            System.out.println("DEBUG: Current island name: " + world.getCurrentIsland().getName());
        }
        
        showIslandSpecificGameSceneInstance(player, world);
    }
    
    private void showSlotSelectionInstance() throws IOException {
        FXMLLoader loader = new FXMLLoader(JavaFXGame.class.getResource("/fxml/slot-selection.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 400, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/common-dark.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/slot-selection.css").toExternalForm());
        primaryStage.setScene(scene);
    }
    
    private void showRetroCombatInstance(Player player, Mob enemy, WorldManager world) throws IOException {
    FXMLLoader loader = new FXMLLoader(JavaFXGame.class.getResource("/fxml/retro-combat.fxml"));
    Parent root = loader.load();
    
    // Переносим получение острова в самый верх метода, чтобы использовать его сразу
    Island currentIsland = world.getCurrentIsland();
    String islandName = (currentIsland != null) ? currentIsland.getName() : "";
    
    // 1. Создаем правильный менеджер в зависимости от текущего острова
    CombatManager combatManager;
    if (islandName.contains("Классов")) { 
        combatManager = new CSharpCombatManager(player, enemy);
    } else if (islandName.contains("Циклов")) { 
        combatManager = new PythonCombatManager(player, enemy);
    } else {
        combatManager = new CombatManager(player, enemy); // Базовый Java-остров
    }

    // 2. Получаем контроллер из загрузчика FXML
    RetroCombatController controller = loader.getController();
    
    // 3. Передаем всё в контроллер (используем локальный combatManager без "this.")
    controller.setCombatData(player, enemy, world, combatManager);
    
    Scene scene = new Scene(root, 480, 640);
    scene.getStylesheets().add(getClass().getResource("/styles/common-dark.css").toExternalForm());
    
    // Настройка стилей интерфейса в зависимости от острова
    if (currentIsland != null) {
        if (islandName.contains("Циклов")) {
            scene.getStylesheets().add(getClass().getResource("/styles/python-retro-combat.css").toExternalForm());
        } else if (islandName.contains("Классов")) {
            scene.getStylesheets().add(getClass().getResource("/styles/csharp-retro-combat.css").toExternalForm());
        } else {
            scene.getStylesheets().add(getClass().getResource("/styles/retro-combat.css").toExternalForm());
        }
    } else {
        scene.getStylesheets().add(getClass().getResource("/styles/retro-combat.css").toExternalForm());
    }
    
    primaryStage.setScene(scene);
    }
    
    private void showShopInstance(Player player, WorldManager world) throws IOException {
        FXMLLoader loader = new FXMLLoader(JavaFXGame.class.getResource("/fxml/shop.fxml"));
        Parent root = loader.load();
        
        ShopController controller = loader.getController();
        controller.setPlayerData(player, world);
        
        Scene scene = new Scene(root, 400, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/common-dark.css").toExternalForm());
        
        Island currentIsland = world.getCurrentIsland();
        if (currentIsland != null) {
            String islandName = currentIsland.getName();
            if (islandName.contains("Циклов")) {
                scene.getStylesheets().add(getClass().getResource("/styles/python-shop.css").toExternalForm());
            } else if (islandName.contains("Классов")) {
                scene.getStylesheets().add(getClass().getResource("/styles/csharp-shop.css").toExternalForm());
            } else {
                scene.getStylesheets().add(getClass().getResource("/styles/shop.css").toExternalForm());
            }
        } else {
            scene.getStylesheets().add(getClass().getResource("/styles/shop.css").toExternalForm());
        }
        
        primaryStage.setScene(scene);
    }

    private void showIslandSpecificGameSceneInstance(Player player, WorldManager world) throws IOException {
        System.out.println("[DEBUG] showIslandSpecificGameSceneInstance called");
        System.out.println("[DEBUG] Player island index: " + player.getCurrentIslandIndex());
        
        showGameSceneInstance(player, world);
    }
    
    private void showAuthScreenInstance() throws IOException {
        FXMLLoader loader = new FXMLLoader(JavaFXGame.class.getResource("/fxml/auth-screen.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 400, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/common-dark.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/auth-styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }
    
    private void showRegistrationScreenInstance() throws IOException {
        FXMLLoader loader = new FXMLLoader(JavaFXGame.class.getResource("/fxml/registration-screen.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 400, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/common-dark.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/auth-styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static void showGuideScreen() throws IOException {
        instance.showGuideScreenInstance();
    }
    
    public static void showSelectIsland() throws IOException {
        instance.showSelectIslandInstance();
    }
    
    public static void showJavaGameScene(Player player) throws IOException {
        player.setCurrentIslandIndex(0); 
        WorldManager world = new WorldManager(0);
        instance.showGameSceneInstance(player, world);
    }
    
    public static void showPythonGameScene(Player player) throws IOException {
        player.setCurrentIslandIndex(1); 
        WorldManager world = new WorldManager(1);
        instance.showGameSceneInstance(player, world);
    }
    
    public static void showCSharpGameScene(Player player) throws IOException {
        player.setCurrentIslandIndex(2); 
        WorldManager world = new WorldManager(2);
        instance.showGameSceneInstance(player, world);
    }
    
    private void showGuideScreenInstance() throws IOException {
        FXMLLoader loader = new FXMLLoader(JavaFXGame.class.getResource("/fxml/guide-screen.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 400, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/common-dark.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/auth-styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }
    
    private void showSelectIslandInstance() throws IOException {
        FXMLLoader loader = new FXMLLoader(JavaFXGame.class.getResource("/fxml/select-island.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 400, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/common-dark.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/select-island.css").toExternalForm());
        primaryStage.setScene(scene);
    }
    
    /* ИЗМЕНЕННЫЙ МЕТОД: Динамически определяет FXML и CSS на основе индекса острова */
    private void showGameSceneInstance(Player player, WorldManager world) throws IOException {
        int islandIndex = player.getCurrentIslandIndex();
        IslandType islandType = IslandType.fromIndex(islandIndex);
        
        // 1. Динамический выбор нужного FXML файла в зависимости от индекса острова
        String fxmlPath;
        switch (islandIndex) {
            case 1:
                fxmlPath = "/fxml/python-game-scene.fxml";
                break;
            case 2:
                fxmlPath = "/fxml/csharp-game-scene.fxml";
                break;
            default:
                fxmlPath = "/fxml/java-game-scene.fxml"; // По умолчанию Java (индекс 0)
                break;
        }
        
        FXMLLoader loader = new FXMLLoader(JavaFXGame.class.getResource(fxmlPath));
        Parent root = loader.load();
        
        // Единный контроллер успешно инициализирует данные на любой из трех сцен,
        // так как все fx:id внутри FXML файлов полностью совпадают!
        GameSceneController controller = loader.getController();
        controller.initializeData(player, world);
        
        // 2. Создание сцены и применение CSS стилей
        Scene scene = new Scene(root, 400, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/common-dark.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource(islandType.getGameSceneCssPath()).toExternalForm());
        
        System.out.println("[DEBUG] Загружена сцена " + fxmlPath + " для острова: " + islandType.getDisplayName() + " с CSS: " + islandType.getGameSceneCssPath());
        
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}