package com.codetravel.entities;

import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {
    private int experience;
    private int currentIslandIndex;
    private String characterClass;
    private int coins;
    
    // Инвентарь предметов
    private Map<String, Integer> inventory;
    
    // Бонусы от предметов и улучшений
    private int damageBonus = 0;
    private double damageMultiplier = 1.0;
    private int defenseBonus = 0;
    private double defenseMultiplier = 1.0;
    private int maxHealthBonus = 0;
    private double expMultiplier = 1.0;

    public Player(String name) {
        super(name, 100, 1);
        this.experience = 0;
        this.currentIslandIndex = 0;
        this.coins = 100; // Начальные монеты
        this.inventory = new HashMap<>();

        // ДОБАВЬ ЭТИ СТРОКИ ДЛЯ ТЕСТА:
        //addItem("Зелье здоровья", 5);
        //addItem("Зелье опыта", 3);
        //addItem("Зелье силы", 2);
    }

    public Player(String name, String characterClass) {
        super(name, 100, 1);
        this.experience = 0;
        this.currentIslandIndex = 0;
        this.characterClass = characterClass;
        this.coins = 100; // Начальные монеты
        this.inventory = new HashMap<>();

        // И СЮДА ТОЖЕ ДЛЯ ТЕСТА:
        //addItem("Зелье здоровья", 5);
        //addItem("Зелье опыта", 3);
        //addItem("Зелье силы", 2);
    }

    @Override
    public void takeDamage(int damage) {
        // Применяем защиту от предметов
        int actualDamage = (int) (damage * defenseMultiplier) - defenseBonus;
        if (actualDamage < 0) actualDamage = 0;
        
        this.health -= actualDamage;
        if (this.health < 0) this.health = 0;
    }
    
    @Override
    public int getMaxHealth() {
        return super.getMaxHealth() + maxHealthBonus;
    }
    
    public int getEffectiveDamage() {
        int baseDamage = 10 + (level * 2); // Базовый урон зависит от уровня
        return (int) ((baseDamage + damageBonus) * damageMultiplier);
    }

    public void addExperience(int exp) {
        int actualExp = (int) (exp * expMultiplier);
        this.experience += actualExp;
        // Проверяем прокачку циклически на случай нескольких уровней
        while (this.experience >= level * 100) {
            levelUp();
        }
    }

    private void levelUp() {
        // Вычитаем требуемый опыт, остаток сохраняется
        int requiredExp = level * 100;
        this.experience -= requiredExp;
        
        level++;
        maxHealth += 20;
        health = maxHealth;
        System.out.println("УРОВЕНЬ ПОВЫШЕН! Теперь вы " + level + " уровня.");
        System.out.println("Остаточный опыт: " + this.experience + "/" + (level * 100));
    }
    
    // ===== МЕТОДЫ ИНВЕНТАРЯ =====
    
    public void addItem(String itemName, int count) {
        inventory.put(itemName, inventory.getOrDefault(itemName, 0) + count);
        System.out.println("Добавлен предмет: " + itemName + " x" + count);
    }
    
    public boolean hasItem(String itemName) {
        return inventory.containsKey(itemName) && inventory.get(itemName) > 0;
    }
    
    public int getItemCount(String itemName) {
        return inventory.getOrDefault(itemName, 0);
    }
    
    public boolean useItem(String itemName) {
        if (hasItem(itemName)) {
            int count = inventory.get(itemName);
            if (count <= 1) {
                inventory.remove(itemName);
            } else {
                inventory.put(itemName, count - 1);
            }
            System.out.println("Использован предмет: " + itemName);
            return true;
        }
        System.out.println("Предмет не найден: " + itemName);
        return false;
    }
    
    // ===== МЕТОДЫ БОНУСОВ =====
    
    public void addDamageBonus(int bonus) {
        this.damageBonus += bonus;
        System.out.println("Бонус к урону: +" + bonus);
    }
    
    public void addDamageMultiplier(double multiplier) {
        this.damageMultiplier *= multiplier;
        System.out.println("Множитель урона: x" + multiplier);
    }
    
    public void addDefenseBonus(int bonus) {
        this.defenseBonus += bonus;
        System.out.println("Бонус к защите: +" + bonus);
    }
    
    public void addDefenseMultiplier(double multiplier) {
        this.defenseMultiplier *= multiplier;
        System.out.println("Множитель защиты: x" + multiplier);
    }
    
    public void addMaxHealthBonus(int bonus) {
        this.maxHealthBonus += bonus;
        this.maxHealth += bonus;
        this.health += bonus;
        System.out.println("Бонус к здоровью: +" + bonus);
    }
    
    public void addExpMultiplier(double multiplier) {
        this.expMultiplier *= multiplier;
        System.out.println("Множитель опыта: x" + multiplier);
    }
    
    // ===== ИСПОЛЬЗОВАНИЕ ПРЕДМЕТОВ =====
    
    public boolean useHealthPotion() {
        if (useItem("Зелье здоровья")) {
            int healAmount = 50;
            this.health = Math.min(this.health + healAmount, getMaxHealth());
            System.out.println("Восстановлено " + healAmount + " HP");
            return true;
        }
        return false;
    }
    
    public boolean useExpPotion() {
        if (useItem("Зелье опыта")) {
            addExperience(100);
            System.out.println("Получено 100 опыта от зелья!");
            return true;
        }
        return false;
    }
    
    public boolean useStrengthPotion() {
        if (useItem("Зелье силы")) {
            addDamageMultiplier(1.2);
            System.out.println("Активирован бонус к урону +20%!");
            return true;
        }
        return false;
    }
    
    public void equipCoderSword() {
        if (useItem("Меч кодера")) {
            addDamageBonus(15);
            System.out.println("Экипирован Меч кодера! +15 к урону");
        }
    }
    
    public void equipProgrammerShield() {
        if (useItem("Щит программиста")) {
            addDefenseMultiplier(0.75);
            System.out.println("Экипирован Щит программиста! -25% к урону");
        }
    }
    
    public boolean useDebuggerArt() {
        if (useItem("Артефакт отладчика")) {
            System.out.println("Активирован Артефакт отладчика! Авто-решение заданий!");
            return true;
        }
        return false;
    }
    
    // Геттеры для инвентаря и бонусов
    public Map<String, Integer> getInventory() { return inventory; }
    public int getDamageBonus() { return damageBonus; }
    public double getDamageMultiplier() { return damageMultiplier; }
    public int getDefenseBonus() { return defenseBonus; }
    public double getDefenseMultiplier() { return defenseMultiplier; }
    public int getMaxHealthBonus() { return maxHealthBonus; }
    public double getExpMultiplier() { return expMultiplier; }

    public int getCurrentIslandIndex() { return currentIslandIndex; }
    public void setCurrentIslandIndex(int index) { this.currentIslandIndex = index; }
    public int getExperience() { return experience; }
    public String getCharacterClass() { return characterClass; }
    public void setCharacterClass(String characterClass) { this.characterClass = characterClass; }
    
    // Методы для работы с монетами
    public int getCoins() { return coins; }
    public void addCoins(int amount) { 
        this.coins += amount;
        System.out.println("Получено монет: " + amount + ". Всего: " + coins);
    }
    public boolean removeCoins(int amount) { 
        if (this.coins >= amount) {
            this.coins -= amount;
            System.out.println("Потрачено монет: " + amount + ". Осталось: " + coins);
            return true;
        }
        System.out.println("Недостаточно монет! Нужно: " + amount + ", есть: " + coins);
        return false;
    }
    public void setCoins(int amount) { this.coins = amount; }
}
