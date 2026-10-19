package com.codetravel.core;

import com.codetravel.entities.Player;

/**
 * Singleton класс для управления состоянием игрока между сценами
 * Обеспечивает сохранение и передачу данных о здоровье, монетах и опыте
 */
public class GameState {
    private static GameState instance;
    private Player currentPlayer;
    
    private GameState() {
        // Private constructor для Singleton
    }
    
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        System.out.println("GameState: Установлен игрок " + player.getName() + 
                          " (HP: " + player.getHealth() + 
                          ", Coins: " + player.getCoins() + 
                          ", Exp: " + player.getExperience() + ")");
    }
    
    public void updatePlayerStats(int healthChange, int coinsChange, int expChange) {
        if (currentPlayer != null) {
            if (healthChange > 0) {
                // Для лечения - используем takeDamage с отрицательным значением
                currentPlayer.takeDamage(-healthChange);
                System.out.println("GameState: HP изменено на +" + healthChange + 
                                  ", текущее HP: " + currentPlayer.getHealth());
            } else if (healthChange < 0) {
                // Для урона - используем takeDamage
                currentPlayer.takeDamage(-healthChange);
                System.out.println("GameState: HP изменено на " + healthChange + 
                                  ", текущее HP: " + currentPlayer.getHealth());
            }
            
            if (coinsChange != 0) {
                currentPlayer.addCoins(coinsChange);
                System.out.println("GameState: Монеты изменены на " + coinsChange + 
                                  ", текущие монеты: " + currentPlayer.getCoins());
            }
            
            if (expChange != 0) {
                currentPlayer.addExperience(expChange);
                System.out.println("GameState: Опыт изменен на " + expChange + 
                                  ", текущий опыт: " + currentPlayer.getExperience());
            }
        }
    }
    
    public void reset() {
        currentPlayer = null;
        System.out.println("GameState: Сброс состояния");
    }
    
    public boolean hasPlayer() {
        return currentPlayer != null;
    }
}
