package com.codetravel.core;

import com.codetravel.entities.Player;
import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.dat";

    public static void saveGame(Player player) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(player);
            System.out.println("Игра сохранена!");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении: " + e.getMessage());
        }
    }
    
    // Сохранение в конкретный слот
    public static void saveGame(Player player, int slotNumber) {
        String fileName = "save_slot_" + slotNumber + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(player);
            System.out.println("Игра сохранена в слот " + slotNumber + "!");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении в слот " + slotNumber + ": " + e.getMessage());
        }
    }

    public static Player loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (Player) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при загрузке: " + e.getMessage());
            return null;
        }
    }
    
    // Загрузка из конкретного слота
    public static Player loadGame(int slotNumber) {
        String fileName = "save_slot_" + slotNumber + ".dat";
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Player) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при загрузке из слота " + slotNumber + ": " + e.getMessage());
            return null;
        }
    }
    
    // Проверка существования сохранения в слоте
    public static boolean hasSaveFile(int slotNumber) {
        String fileName = "save_slot_" + slotNumber + ".dat";
        return new File(fileName).exists();
    }
}
