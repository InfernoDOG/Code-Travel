package com.codetravel.ui;

import com.codetravel.entities.Player;
import com.codetravel.levels.WorldManager;
import com.codetravel.core.JavaFXGame;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для магазина игры
 */
public class ShopController {
    
    @FXML private Label playerCoinsLabel;
    @FXML private Label messageLabel;
    
    // Кнопки зелий
    @FXML private Button buyHealthPotion;
    @FXML private Button buyExpPotion;
    @FXML private Button buyStrengthPotion;
    
    // Кнопки предметов
    @FXML private Button buyCoderSword;
    @FXML private Button buyProgrammerShield;
    @FXML private Button buyDebuggerArt;
    
    // Кнопки улучшений
    @FXML private Button buyHpUpgrade;
    @FXML private Button buyDamageUpgrade;
    @FXML private Button buyExpUpgrade;
    
    // Цены
    @FXML private Label healthPotionPrice;
    @FXML private Label expPotionPrice;
    @FXML private Label strengthPotionPrice;
    @FXML private Label coderSwordPrice;
    @FXML private Label programmerShieldPrice;
    @FXML private Label debuggerArtPrice;
    @FXML private Label hpUpgradePrice;
    @FXML private Label damageUpgradePrice;
    @FXML private Label expUpgradePrice;
    
    @FXML private Button backButton;
    
    private Player player;
    private WorldManager worldManager;
    
    // Цены предметов
    private final Map<String, Integer> prices = new HashMap<>();
    
    @FXML
    public void initialize() {
        setupPrices();
        setupEventHandlers();
    }
    
    public void setPlayerData(Player player, WorldManager worldManager) {
        this.player = player;
        this.worldManager = worldManager;
        updateCoinsDisplay();
    }
    
    private void setupPrices() {
        prices.put("healthPotion", 50);
        prices.put("expPotion", 75);
        prices.put("strengthPotion", 100);
        prices.put("coderSword", 150);
        prices.put("programmerShield", 125);
        prices.put("debuggerArt", 300);
        prices.put("hpUpgrade", 200);
        prices.put("damageUpgrade", 250);
        prices.put("expUpgrade", 225);
        
        // Обновляем отображение цен
        healthPotionPrice.setText("💰 " + prices.get("healthPotion"));
        expPotionPrice.setText("💰 " + prices.get("expPotion"));
        strengthPotionPrice.setText("💰 " + prices.get("strengthPotion"));
        coderSwordPrice.setText("💰 " + prices.get("coderSword"));
        programmerShieldPrice.setText("💰 " + prices.get("programmerShield"));
        debuggerArtPrice.setText("💰 " + prices.get("debuggerArt"));
        hpUpgradePrice.setText("💰 " + prices.get("hpUpgrade"));
        damageUpgradePrice.setText("💰 " + prices.get("damageUpgrade"));
        expUpgradePrice.setText("💰 " + prices.get("expUpgrade"));
    }
    
    private void setupEventHandlers() {
        // Зелья
        buyHealthPotion.setOnAction(e -> buyItem("healthPotion", "Зелье здоровья"));
        buyExpPotion.setOnAction(e -> buyItem("expPotion", "Зелье опыта"));
        buyStrengthPotion.setOnAction(e -> buyItem("strengthPotion", "Зелье силы"));
        
        // Предметы
        buyCoderSword.setOnAction(e -> buyItem("coderSword", "Меч кодера"));
        buyProgrammerShield.setOnAction(e -> buyItem("programmerShield", "Щит программиста"));
        buyDebuggerArt.setOnAction(e -> buyItem("debuggerArt", "Артефакт отладчика"));
        
        // Улучшения
        buyHpUpgrade.setOnAction(e -> buyItem("hpUpgrade", "Улучшение HP"));
        buyDamageUpgrade.setOnAction(e -> buyItem("damageUpgrade", "Улучшение урона"));
        buyExpUpgrade.setOnAction(e -> buyItem("expUpgrade", "Улучшение опыта"));
        
        // Кнопка назад
        backButton.setOnAction(e -> {
            try {
                JavaFXGame.showIslandSpecificGameScene(player, worldManager);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    
    private void buyItem(String itemKey, String itemName) {
        if (player == null) {
            showMessage("Ошибка: игрок не найден!", "error");
            return;
        }
        
        int price = prices.get(itemKey);
        int playerCoins = getPlayerCoins();
        
        if (playerCoins < price) {
            showMessage("Недостаточно монет! Нужно: " + price + " 💰", "error");
            return;
        }
        
        // Вычитаем монеты
        if (removePlayerCoins(price)) {
            // Применяем эффект предмета
            applyItemEffect(itemKey);
            updateCoinsDisplay();
            showMessage("Куплено: " + itemName + "! ✨", "success");
            
            // Анимация монет
            animateCoins();
        } else {
            showMessage("Ошибка при покупке!", "error");
        }
    }
    
    private void applyItemEffect(String itemKey) {
    switch (itemKey) {
        case "healthPotion":
            // Кладем зелье в инвентарь, чтобы использовать его в бою
            player.addItem("Зелье здоровья", 1);
            showMessage("Зелье здоровья добавлено в инвентарь! 🧪", "success");
            break;
            
        case "expPotion":
            // Кладем зелье в инвентарь
            player.addItem("Зелье опыта", 1);
            showMessage("Зелье опыта добавлено в инвентарь! ⭐", "success");
            break;
            
        case "strengthPotion":
            // Кладем зелье в инвентарь
            player.addItem("Зелье силы", 1);
            showMessage("Зелье силы добавлено в инвентарь! 💪", "success");
            break;
            
        case "coderSword":
            // Постоянное увеличение урона (+15)
            player.addItem("Меч кодера", 1);
            showMessage("Меч кодера добавлен! Урон +15 ⚔️", "success");
            break;
            
        case "programmerShield":
            // Постоянное снижение получаемого урона (-25%)
            player.addItem("Щит программиста", 1);
            showMessage("Щит программиста добавлен! Урон -25% 🛡️", "success");
            break;
            
        case "debuggerArt":
            // Авто-решение заданий
            player.addItem("Артефакт отладчика", 1);
            showMessage("Артефакт отладчика добавлен! Авто-решение заданий 🔮", "success");
            break;
            
        case "hpUpgrade":
            // Постоянное увеличение макс. здоровья (+25)
            player.addMaxHealthBonus(25);
            showMessage("Макс. здоровье увеличено на 25! ❤️‍🔥", "success");
            break;
            
        case "damageUpgrade":
            // Постоянное увеличение урона (+10%)
            player.addDamageMultiplier(1.1);
            showMessage("Урон увеличен на 10% навсегда! ⚡", "success");
            break;
            
        case "expUpgrade":
            // Постоянное увеличение опыта (+25%)
            player.addExpMultiplier(1.25);
            showMessage("Получаемый опыт увеличен на 25% навсегда! 🌟", "success");
            break;
    }
}
    
    private int getPlayerCoins() {
        if (player != null) {
            return player.getCoins();
        }
        return 0;
    }
    
    private boolean removePlayerCoins(int amount) {
        if (player != null) {
            return player.removeCoins(amount);
        }
        return false;
    }
    
    private void updateCoinsDisplay() {
        int coins = getPlayerCoins();
        playerCoinsLabel.setText("💰 Монеты: " + coins);
    }
    
    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().removeAll("error", "success", "warning");
        messageLabel.getStyleClass().add(type);
        
        // Очищаем сообщение через 3 секунды
        javafx.animation.PauseTransition timer = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
        timer.setOnFinished(e -> {
            messageLabel.setText("");
            messageLabel.getStyleClass().removeAll("error", "success", "warning");
        });
        timer.play();
    }
    
    private void animateCoins() {
        // Анимация пульсации монет
        playerCoinsLabel.getStyleClass().add("coins-pulse");
        
        javafx.animation.PauseTransition timer = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
        timer.setOnFinished(e -> {
            playerCoinsLabel.getStyleClass().remove("coins-pulse");
        });
        timer.play();
    }
}
