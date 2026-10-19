package com.codetravel.ui;

import com.codetravel.combat.CodeChallenge;
import com.codetravel.combat.CombatManager;
import com.codetravel.core.JavaFXGame;
import com.codetravel.entities.Player;
import com.codetravel.entities.Mob;
import com.codetravel.levels.WorldManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для ретро-RPG боевой сцены в стиле пиксельной графики
 */
public class RetroCombatController {
    
    @FXML private Canvas battleCanvas;
    @FXML private Label messageLabel;
    @FXML private Label taskLabel;
    @FXML private TextField answerField;
    @FXML private Button fightButton; 
    @FXML private Label playerHPLabel;
    @FXML private Label enemyHPLabel;
    @FXML private Button useHealthPotionBtn;
    @FXML private Button useExpPotionBtn;
    @FXML private Button useStrengthPotionBtn;
    @FXML private Label itemsCountLabel;
    @FXML private Button hintButton;
    @FXML private Button runButton;
    @FXML private ProgressBar healthBar;
    
    private Player player;
    private Mob enemy;
    private WorldManager worldManager;
    private CombatManager combatManager;
    
    private int playerHP = 100;
    private int enemyHP = 100;
    private CodeChallenge currentTask;
    private List<CodeChallenge> database = new ArrayList<>();
    private Random random = new Random();
    private int hintCount = 0; 
    
    // ===== ПОЛЯ ТЕКСТУР =====
    private Image playerTexture;
    private Image enemyTexture;
    private Image battleBackground;
    
    // ===== ПЕРЕМЕННЫЕ ХОДОВ =====
    private boolean isPlayerTurn = true;
    private boolean turnInProgress = false;
    
    @FXML
    public void initialize() {
        // Настройка обработчиков событий
        setupEventHandlers();
        
        // Настройка автоматической активации кнопки
        setupAutoActivation();

        // ОБЯЗАТЕЛЬНО В КОНЦЕ:
        updateItemsUI();
        drawBattleScene();
    }

    private void updateLog(String message) {
        messageLabel.setText(message);
    }
    
    private void loadTextures() {
        try {
            battleBackground = new Image(getClass().getResourceAsStream("/texture/combat/combat-bg.png"));
            enemyTexture = null;
            
            if (player != null && player.getCharacterClass() != null) {
                String playerClass = player.getCharacterClass().toLowerCase();
                
                if (playerClass.contains("воин")) {
                    playerTexture = new Image(getClass().getResourceAsStream("/texture/Character/Knight/knight.png"));
                } else if (playerClass.contains("маг")) {
                    playerTexture = new Image(getClass().getResourceAsStream("/texture/Character/Mage/mage.png"));
                } else if (playerClass.contains("жрец")) {
                    playerTexture = new Image(getClass().getResourceAsStream("/texture/Character/Prist/data-priest.png"));
                } else if (playerClass.contains("разработчик")) {
                    playerTexture = new Image(getClass().getResourceAsStream("/texture/Character/Developer/Developer.png"));
                } else {
                    playerTexture = new Image(getClass().getResourceAsStream("/texture/Character/Knight/knight.png"));
                }
            } else {
                playerTexture = new Image(getClass().getResourceAsStream("/texture/Character/Knight/knight.png"));
            }
            
            if (enemy != null) {
                String enemyName = enemy.getName().toLowerCase();
                
                if (enemyName.contains("дабл") || enemyName.contains("змея")) {
                    enemyTexture = new Image(getClass().getResourceAsStream("/texture/mobs/double-snake.png"));
                } else if (enemyName.contains("слайм")) {
                    enemyTexture = new Image(getClass().getResourceAsStream("/texture/mobs/slime-int.png"));
                } else if (enemyName.contains("лорд")) {
                    enemyTexture = new Image(getClass().getResourceAsStream("/texture/mobs/lord-string.png"));
                } else {
                    enemyTexture = new Image(getClass().getResourceAsStream("/texture/mobs/slime-int.png"));
                }
            } else {
                enemyTexture = new Image(getClass().getResourceAsStream("/texture/mobs/slime-int.png"));
            }
            
            String[] backgrounds = {"cave.jpg", "ruins.jpg", "plant.jpg"};
            String randomBg = backgrounds[new Random().nextInt(backgrounds.length)];
            battleBackground = new Image(getClass().getResourceAsStream("/texture/battle bg/" + randomBg));
            
        } catch (Exception e) {
            System.out.println("Ошибка загрузки текстур: " + e.getMessage());
            playerTexture = null;
            enemyTexture = null;
            battleBackground = null;
        }
    }
    
    public void setCombatData(Player player, Mob enemy, WorldManager world, CombatManager combatManager) {
        this.player = player;
        this.enemy = enemy;
        this.worldManager = world;
        this.combatManager = combatManager;

        if (combatManager != null) {
            this.database = combatManager.getTasks(); 
        }
        
        this.playerHP = player.getHealth();
        this.enemyHP = enemy.getHealth();
        
        messageLabel.setText("Дикий " + enemy.getName() + " преграждает путь!");
        
        // ===== КОНСОЛЬНАЯ ДИАГНОСТИКА ДЛЯ ОПТИМИЗАЦИИ =====
        System.out.println("\n=== [DIAGNOSTIC] ПРОВЕРКА ИНВЕНТАРЯ В БОЮ ===");
        if (this.player != null) {
            System.out.println("Текущий объект игрока: " + this.player.toString());
            System.out.println("Зелий здоровья: " + this.player.getItemCount("Зелье здоровья"));
            System.out.println("Зелий опыта: " + this.player.getItemCount("Зелье опыта"));
            System.out.println("Зелий силы: " + this.player.getItemCount("Зелье силы"));
        } else {
            System.out.println("Критическая ошибка: Объект игрока равен null!");
        }
        System.out.println("=============================================\n");
        
        loadTextures();
        updateItemsUI();
        updateHPLabels();
        drawBattleScene();
        startTurn();
    }
    
    private void setupEventHandlers() {
        fightButton.setOnAction(e -> handleAttack(e));
        fightButton.setOnMouseClicked(e -> handleAttack(e));
        answerField.setOnAction(e -> processAnswer());
        hintButton.setOnAction(e -> showHint());
        runButton.setOnAction(e -> runAway());
        
        // ИСПРАВЛЕНИЕ: Обязательно привязываем клики по кнопкам к логике!
        useHealthPotionBtn.setOnAction(e -> handleUseHealthPotion());
        useExpPotionBtn.setOnAction(e -> handleUseExpPotion());
        useStrengthPotionBtn.setOnAction(e -> handleUseStrengthPotion());
    }

    // ===== ОБРАБОТЧИКИ КНОПОК ПРЕДМЕТОВ =====

    private void handleUseHealthPotion() {
        if (player != null && player.useHealthPotion()) {
            playerHP = player.getHealth(); 
            if (healthBar != null) {
                healthBar.setProgress((double) player.getHealth() / player.getMaxHealth());
            }
            updateHPLabels();
            updateItemsUI();
            messageLabel.setText("✨ Использовано зелье здоровья! +50 HP");
            drawBattleScene();
        } else {
            messageLabel.setText("❌ Зелья здоровья нет!");
        }
    }

    private void handleUseExpPotion() {
        if (player != null && player.useExpPotion()) {
            updateItemsUI();
            messageLabel.setText("✨ Использовано зелье опыта! +100 EXP");
        } else {
            messageLabel.setText("❌ Зелья опыта нет!");
        }
    }

    private void handleUseStrengthPotion() {
        if (player != null && player.useStrengthPotion()) {
            updateItemsUI();
            messageLabel.setText("✨ Активировано зелье силы! +20% к урону");
        } else {
            messageLabel.setText("❌ Зелья силы нет!");
        }
    }
    
    @FXML
    private void handleAttack(javafx.event.Event event) {
        String userAnswer = answerField.getText().trim();
        
        if (userAnswer.isEmpty()) {
            messageLabel.setText("❌ Сначала введите ответ!");
            return;
        }
        
        answerField.setDisable(true);
        fightButton.setDisable(true);
        
        if (userAnswer.equalsIgnoreCase(currentTask.getAnswer())) { 
            messageLabel.setText("✨ Отлично! Нанесено " + currentTask.getDamage() + " урона!"); 
            enemyHP -= currentTask.getDamage(); 
            answerField.clear();
            
            if (enemyHP <= 0) {
                victory();
            } else {
                isPlayerTurn = false;
                turnInProgress = false;
                enemyAttack("⚔️ Враг контратакует!");
            }
        } else {
            messageLabel.setText("❌ Неправильно! Враг наносит 10 урона!");
            playerHP -= 10;
            if (playerHP < 0) playerHP = 0;
            updateHPLabels();
            drawBattleScene();
            answerField.clear();
            
            if (playerHP <= 0) {
                defeat();
            } else {
                turnInProgress = false;
                startTurn(); 
            }
        }
        
        fightButton.setStyle("-fx-background-color: #1B5E20; -fx-scale-x: 0.95; -fx-scale-y: 0.95;");
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> fightButton.setStyle(""));
        pause.play();
    }
    
    private void setupAutoActivation() {
        answerField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() && fightButton.isDisabled() && isPlayerTurn) {
                fightButton.setDisable(false);
            }
        });
    }
    
    private void updateItemsUI() {
        if (player != null) {
            int health = player.getItemCount("Зелье здоровья");
            int exp = player.getItemCount("Зелье опыта");
            int strength = player.getItemCount("Зелье силы");
            
            itemsCountLabel.setText("Предметы: " + (health + exp + strength));
            
            useHealthPotionBtn.setDisable(health <= 0);
            useExpPotionBtn.setDisable(exp <= 0);
            useStrengthPotionBtn.setDisable(strength <= 0);
            
            useHealthPotionBtn.setText("❤");
            useExpPotionBtn.setText("✨");
            useStrengthPotionBtn.setText("💪");
        }
    }
    
    // ===== МЕТОДЫ ПОДСКАЗОК И ПОБЕГА =====
    
    private void showHint() {
        if (currentTask == null) {
            messageLabel.setText("❌ Сначала начните битву!");
            return;
        }
        
        if (hintCount >= 3) {
            messageLabel.setText("❌ Больше нет подсказок!");
            return;
        }
        
        hintCount++;
        String question = currentTask.getQuestion(); 
        String answer = currentTask.getAnswer(); 
        String hint = "";
        
        switch (hintCount) {
            case 1:
                hint = generateHint1(question, answer);
                hintButton.setText("💡 ПОДСКАЗКА 2");
                break;
            case 2:
                hint = generateHint2(question, answer);
                hintButton.setText("💡 ПОДСКАЗКА 3");
                break;
            case 3:
                hint = generateHint3(question, answer);
                hintButton.setDisable(true);
                hintButton.setText("💡 ПОДСКАЗКИ ИСЧЕРПАНЫ");
                break;
        }
        
        messageLabel.setText("💡 Подсказка " + hintCount + ": " + hint);
    }
    
    private String generateHint1(String question, String answer) {
        if (question.contains("объявить")) return "Нужно использовать тип данных и имя переменной";
        if (question.contains("оператор")) return "Это математический или логический оператор";
        if (question.contains("метод")) return "Это стандартный метод Java";
        if (question.contains("тип данных")) return "Примитивный тип для чисел или символов";
        return "Внимательно прочитайте вопрос";
    }
    
    private String generateHint2(String question, String answer) {
        if (question.contains("целочисленную")) return "Тип данных начинается с буквы 'i'";
        if (question.contains("сравнения")) return "Используется для проверки равенства двух значений";
        if (question.contains("точкой входа")) return "Этот метод вызывается при запуске программы";
        if (question.contains("дробных чисел")) return "Тип для чисел с плавающей точкой";
        return "Ответ содержит " + answer.length() + " символов";
    }
    
    private String generateHint3(String question, String answer) {
        if (question.contains("целочисленную")) return "int x = 5;";
        if (question.contains("сравнения")) return "Используйте два знака равенства";
        if (question.contains("точкой входа")) return "main";
        if (question.contains("дробных чисел")) return "double";
        return "Ответ: " + answer;
    }
    
    private void runAway() {
        messageLabel.setText("🏃 Вы сбежали из битвы!");
        player.takeDamage(10);
        playerHP = player.getHealth();
        updateHPLabels();
        
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            try {
                JavaFXGame.showIslandSpecificGameScene(player, worldManager); 
            } catch (IOException ex) {
                messageLabel.setText("❌ Ошибка при возврате в игру!");
                ex.printStackTrace();
            }
        });
        delay.play();
    }
    
    private void drawBattleScene() {
        GraphicsContext gc = battleCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, battleCanvas.getWidth(), battleCanvas.getHeight());
        
        if (battleBackground != null) {
            gc.drawImage(battleBackground, 0, 0, battleCanvas.getWidth(), battleCanvas.getHeight());
        } else {
            gc.setFill(Color.rgb(128, 208, 160));
            gc.fillRect(0, 0, battleCanvas.getWidth(), battleCanvas.getHeight());
        }
        
        gc.setFill(Color.rgb(128, 128, 128, 0.4)); 
        gc.fillOval(40, 220, 180, 60);  
        gc.fillOval(200, 60, 140, 40);  
        
        if (playerTexture != null) {
            gc.drawImage(playerTexture, 60, 140, 120, 120);
        } else {
            gc.setFill(Color.rgb(41, 128, 185));
            gc.fillRect(80, 160, 80, 80);
        }
        
        if (enemyTexture != null) {
            double enemyWidth = 100, enemyHeight = 100;
            if (enemy != null) {
                String enemyName = enemy.getName().toLowerCase();
                if (enemyName.contains("лорд")) {
                    enemyWidth = 140; enemyHeight = 140;
                    gc.drawImage(enemyTexture, 220, 40, enemyWidth, enemyHeight);
                } else if (enemyName.contains("дабл") || enemyName.contains("змея")) {
                    enemyWidth = 120; enemyHeight = 80;
                    gc.drawImage(enemyTexture, 230, 70, enemyWidth, enemyHeight);
                } else {
                    gc.drawImage(enemyTexture, 240, 60, enemyWidth, enemyHeight);
                }
            } else {
                gc.drawImage(enemyTexture, 240, 60, enemyWidth, enemyHeight);
            }
        } else {
            gc.setFill(Color.rgb(39, 174, 96));
            gc.fillRect(260, 80, 60, 60);
        }
        
        String enemyName = (enemy != null) ? enemy.getName() : "ENEMY";
        drawHPBar(gc, 20, 20, enemyName + ".EXE", enemyHP, false); 
        drawHPBar(gc, 140, 200, "USER_ROOT", playerHP, true); 
    }
    
    private void drawHPBar(GraphicsContext gc, int x, int y, String name, int hp, boolean isPlayer) {
        gc.setFill(Color.rgb(20, 24, 42, 0.85));
        gc.fillRect(x, y, 220, 70);
        
        gc.setStroke(isPlayer ? Color.rgb(52, 152, 219) : Color.rgb(231, 76, 60));
        gc.setLineWidth(1.5);
        gc.strokeRect(x, y, 220, 70);
        
        gc.setTextAlign(javafx.scene.text.TextAlignment.LEFT);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Consolas", 14));
        gc.fillText(name, x + 10, y + 20);
        
        gc.setFill(Color.rgb(26, 29, 36));
        gc.fillRect(x + 10, y + 35, 200, 15);
        
        int maxHp = 100; 
        double hpRatio = (double) hp / maxHp;
        hpRatio = Math.min(1.0, Math.max(0.0, hpRatio));
        
        if (hpRatio > 0.5) {
            gc.setFill(isPlayer ? Color.rgb(46, 204, 113) : Color.rgb(230, 126, 34));
        } else if (hpRatio > 0.25) {
            gc.setFill(Color.rgb(241, 196, 15)); 
        } else {
            gc.setFill(Color.rgb(231, 76, 60));  
        }
        
        gc.fillRect(x + 10, y + 35, (int)(hpRatio * 200), 15);
        
        gc.setFill(Color.rgb(189, 195, 199));
        gc.setFont(new Font("Consolas", 12));
        gc.fillText(hp + "/" + maxHp, x + 155, y + 63);
    }
    
    private void updateHPLabels() {
        playerHPLabel.setText("USER_ROOT: " + playerHP + "/100");
        enemyHPLabel.setText(((enemy != null) ? enemy.getName() : "ENEMY") + ".EXE: " + enemyHP + "/100");
    }
    
    private void startTurn() {
        if (turnInProgress) return;
        turnInProgress = true;
        
        if (isPlayerTurn) {
            this.currentTask = database.get(random.nextInt(database.size()));
            taskLabel.setText(currentTask.getQuestion());
            answerField.setDisable(false);
            answerField.requestFocus();
            messageLabel.setText("🎮 ХОД ИГРОКА: Введите ответ:");
            
            hintCount = 0;
            hintButton.setDisable(false);
            hintButton.setText("💡 ПОДСКАЗКА");
        } else {
            messageLabel.setText("👾 ХОД ВРАГА: Моб готовит атаку...");
            answerField.setDisable(true);
            fightButton.setDisable(true);
            hintButton.setDisable(true);
            
            PauseTransition enemyDelay = new PauseTransition(Duration.seconds(2));
            enemyDelay.setOnFinished(e -> {
                enemyAttack("👾 Враг применяет MALWARE_ATTACK!");
                if (playerHP > 0 && enemyHP > 0) {
                    isPlayerTurn = true;
                    turnInProgress = false;
                    startTurn();
                } else {
                    turnInProgress = false;
                }
            });
            enemyDelay.play();
            return;
        }
        drawBattleScene();
    }
    
    private void processAnswer() {
        String userAnswer = answerField.getText().trim();
        if (userAnswer.isEmpty()) {
            messageLabel.setText("❌ Введите ответ!");
            return;
        }
        
        answerField.setDisable(true);
        fightButton.setDisable(true);
        
        if (userAnswer.equalsIgnoreCase(currentTask.getAnswer())) { 
            playerAttack();
        } else {
            enemyAttack("❌ ОШИБКА КОМПИЛЯЦИИ! Враг контратакует!");
        }
    }
    
    private void playerAttack() {
        messageLabel.setText("⚔️ USER_ROOT применяет DEBUGGING! Урон: " + currentTask.getDamage()); 
        enemyHP -= currentTask.getDamage();
        if (enemyHP < 0) enemyHP = 0;
        updateHPLabels();
        drawBattleScene();
        
        if (enemyHP <= 0) {
            victory();
        } else {
            isPlayerTurn = false;
            turnInProgress = false;
            
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(e -> startTurn());
            delay.play();
        }
    }
    
    private void enemyAttack(String message) {
        messageLabel.setText(message);
        playerHP -= 15;
        if (playerHP < 0) playerHP = 0;
        updateHPLabels();
        drawBattleScene();
        
        if (playerHP <= 0) {
            defeat();
        } else {
            if (!isPlayerTurn) {
                isPlayerTurn = true;
                turnInProgress = false;
                startTurn(); 
            }
        }
    }
    
    private void victory() {
        messageLabel.setText("🎉 ПОБЕДА! Враг повержен!");
        player.addExperience(50);
        player.addCoins(10);
        fightButton.setDisable(true);
        answerField.setDisable(true);
        
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            try {
                JavaFXGame.showGameScene(player, worldManager);
            } catch (IOException ex) {
                messageLabel.setText("❌ Ошибка при возврате в игру!");
            }
        });
        delay.play();
    }
    
    private void defeat() {
        messageLabel.setText("💀 ПОРАЖЕНИЕ! Вы проиграли битву!");
        fightButton.setDisable(true);
        answerField.setDisable(true);
        
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            try {
                JavaFXGame.showGameScene(player, worldManager);
            } catch (IOException ex) {
                messageLabel.setText("❌ Ошибка при возврате в игру!");
            }
        });
        delay.play();
    }
}