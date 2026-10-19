package com.codetravel.combat;

import com.codetravel.entities.Mob;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Фабрика уникальных мобов для Python острова
 */
public class PythonMobs {
    
    private static final Random random = new Random();
    
    public static List<Mob> getAllPythonMobs() {
        List<Mob> mobs = new ArrayList<>();
        
        // Обычные мобы
        mobs.add(new Mob("Python Cobra", 50, 1, "🐍 Быстрый и ядовитый змей с синтаксисом Python", Mob.Type.NORMAL));
        mobs.add(new Mob("Syntax Spider", 60, 2, "🕷️ Паук, который запутывает в коде", Mob.Type.NORMAL));
        mobs.add(new Mob("Indentation Lizard", 45, 1, "🦎 Ящерица, которая меняет отступы", Mob.Type.NORMAL));
        mobs.add(new Mob("List Snake", 55, 2, "🐍 Змей, живущий в списках Python", Mob.Type.NORMAL));
        mobs.add(new Mob("Dictionary Turtle", 65, 3, "🐢 Черепаха с ключами-значениями", Mob.Type.NORMAL));
        mobs.add(new Mob("Function Falcon", 70, 3, "🦅 Сокол, который летит через функции", Mob.Type.NORMAL));
        mobs.add(new Mob("Import Eagle", 75, 4, "🦅 Орел, приносящий модули", Mob.Type.NORMAL));
        mobs.add(new Mob("Exception Eel", 80, 4, "🐍 Угорь, который бросает исключения", Mob.Type.NORMAL));
        
        // Мини-боссы
        mobs.add(new Mob("Lambda Shark", 120, 5, "🦈 Акула с анонимными функциями", Mob.Type.MINI_BOSS));
        mobs.add(new Mob("Decorator Dragon", 150, 6, "🐉 Дракон с декораторами", Mob.Type.MINI_BOSS));
        mobs.add(new Mob("Generator Griffin", 140, 6, "🦅 Грифон с генераторами", Mob.Type.MINI_BOSS));
        mobs.add(new Mob("Async Phoenix", 160, 7, "🦥 Феникс с асинхронным кодом", Mob.Type.MINI_BOSS));
        
        // Главный босс
        mobs.add(new Mob("Anaconda Boss", 300, 10, "🐍 Огромная и опасная Анаконда - владычица Python острова", Mob.Type.BOSS));
        
        return mobs;
    }
    
    public static Mob getRandomPythonMob(int playerLevel) {
        List<Mob> availableMobs = new ArrayList<>();
        
        for (Mob mob : getAllPythonMobs()) {
            // Добавляем мобов подходящего уровня
            if (mob.getLevel() <= playerLevel + 2 && mob.getLevel() >= playerLevel - 1) {
                availableMobs.add(mob);
            }
        }
        
        if (availableMobs.isEmpty()) {
            // Если нет подходящих мобов, берем самого слабого
            availableMobs.add(getAllPythonMobs().get(0));
        }
        
        return availableMobs.get(random.nextInt(availableMobs.size()));
    }
    
    public static Mob getPythonBoss() {
        return new Mob("Anaconda Boss", 300, 10, "🐍 Огромная и опасная Анаконда - владычица Python острова", Mob.Type.BOSS);
    }
}
