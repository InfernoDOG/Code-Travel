package com.codetravel.levels;

import com.codetravel.entities.Mob;
import java.util.ArrayList;
import java.util.List;

public class WorldManager {
    private List<Island> islands;
    private int currentIslandIndex;

    public WorldManager(int startIndex) {
        this.islands = new ArrayList<>();
        this.currentIslandIndex = startIndex;
        initializeWorld();
    }

    public WorldManager() {
        this.islands = new ArrayList<>();
        this.currentIslandIndex = 0;
        initializeWorld();
    }
    
    public void generateIslands() {
        initializeWorld();
    }

    private void initializeWorld() {
        // Остров 1: Основы (Переменные и типы)
        Island basicsIsland = new Island("Остров Переменных", "Место, где рождаются данные.");
        basicsIsland.addMob(new Mob("Слайм-Инт", 30, 1, "Слабый, но назойливый.", Mob.Type.NORMAL));
        basicsIsland.addMob(new Mob("Дабл-Змей", 40, 2, "Его трудно поймать (true/false).", Mob.Type.NORMAL));
        basicsIsland.addMob(new Mob("Лорд Стринг", 100, 3, "Хранитель текстовых потоков.", Mob.Type.BOSS));
        islands.add(basicsIsland);

        // Остров 2: Циклы (Control Flow)
        Island loopsIsland = new Island("Архипелаг Циклов", "Здесь время идет по кругу.");
        loopsIsland.addMob(new Mob("Вайлик", 60, 4, "Будет атаковать, пока условие истинно.", Mob.Type.NORMAL));
        loopsIsland.addMob(new Mob("Итератор-Страж", 150, 5, "Мастер прохода по массивам.", Mob.Type.MINI_BOSS));
        islands.add(loopsIsland);
        
        // Остров 3: Классы (Объекты и структуры)
        Island classesIsland = new Island("Остров Классов", "Место, где рождаются объекты.");
        classesIsland.addMob(new Mob("Конструктор-Голем", 80, 6, "Создаёт экземпляры без остановки.", Mob.Type.NORMAL));
        classesIsland.addMob(new Mob("Наследник-Призрак", 120, 7, "Копирует свойства предков.", Mob.Type.NORMAL));
        classesIsland.addMob(new Mob("Архитектор-Титан", 200, 8, "Владыка паттернов проектирования.", Mob.Type.BOSS));
        islands.add(classesIsland);
    }

    public Island getCurrentIsland() {
        return islands.get(currentIslandIndex);
    }

    public boolean nextIsland() {
        if (currentIslandIndex < islands.size() - 1) {
            currentIslandIndex++;
            return true;
        }
        return false;
    }
}
