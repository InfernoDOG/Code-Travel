package com.codetravel.combat;

import com.codetravel.entities.Mob;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Фабрика уникальных мобов для C# острова
 */
public class CSharpMobs {
    
    private static final Random random = new Random();
    
    public static List<Mob> getAllCSharpMobs() {
        List<Mob> mobs = new ArrayList<>();
        
        // Обычные мобы
        mobs.add(new Mob(".NET Goblin", 55, 1, "🖥️ Корпоративный монстр .NET", Mob.Type.NORMAL));
        mobs.add(new Mob("Window Warrior", 60, 2, "🪟 Воин, который бьет стеклянными панелями", Mob.Type.NORMAL));
        mobs.add(new Mob("Database Dragon", 65, 2, "🗄️ Дракон, который хранит данные в подземелье", Mob.Type.NORMAL));
        mobs.add(new Mob("Interface Imp", 50, 1, "👹 Бес, который реализует интерфейсы", Mob.Type.NORMAL));
        mobs.add(new Mob("Property Phantom", 58, 2, "👻 Призрак со свойствами C#", Mob.Type.NORMAL));
        mobs.add(new Mob("Delegate Demon", 62, 3, "👹 Демон с делегатами", Mob.Type.NORMAL));
        mobs.add(new Mob("Event Elemental", 68, 3, "🌪️ Элементаль с событиями", Mob.Type.NORMAL));
        mobs.add(new Mob("Generic Golem", 70, 4, "🗿 Голем с дженериками", Mob.Type.NORMAL));
        mobs.add(new Mob("LINQ Lich", 75, 4, "💀 Лич с LINQ запросами", Mob.Type.NORMAL));
        mobs.add(new Mob("Async Assassin", 80, 5, "🗡️ Ассасин с асинхронным кодом", Mob.Type.NORMAL));
        
        // Мини-боссы
        mobs.add(new Mob("WPF Werewolf", 130, 6, "🐺 Оборотень с Windows Presentation Foundation", Mob.Type.MINI_BOSS));
        mobs.add(new Mob("Entity Framework Efreet", 145, 7, "🔥 Ифрит с Entity Framework", Mob.Type.MINI_BOSS));
        mobs.add(new Mob("Unity Unicorn", 135, 6, "🦄 Единорог с Unity game development", Mob.Type.MINI_BOSS));
        mobs.add(new Mob("Xamarin Xorn", 150, 7, "🐉 Ксорн с Xamarin мобильной разработкой", Mob.Type.MINI_BOSS));
        
        // Главный босс
        mobs.add(new Mob("Visual Studio Behemoth", 350, 12, "💻 Огромный IDE босс - владыка C# острова", Mob.Type.BOSS));
        
        return mobs;
    }
    
    public static Mob getRandomCSharpMob(int playerLevel) {
        List<Mob> availableMobs = new ArrayList<>();
        
        for (Mob mob : getAllCSharpMobs()) {
            // Добавляем мобов подходящего уровня
            if (mob.getLevel() <= playerLevel + 2 && mob.getLevel() >= playerLevel - 1) {
                availableMobs.add(mob);
            }
        }
        
        if (availableMobs.isEmpty()) {
            // Если нет подходящих мобов, берем самого слабого
            availableMobs.add(getAllCSharpMobs().get(0));
        }
        
        return availableMobs.get(random.nextInt(availableMobs.size()));
    }
    
    public static Mob getCSharpBoss() {
        return new Mob("Visual Studio Behemoth", 350, 12, "💻 Огромный IDE босс - владыка C# острова", Mob.Type.BOSS);
    }
}
