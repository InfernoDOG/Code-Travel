package com.codetravel.combat;

import com.codetravel.entities.Mob;
import com.codetravel.entities.Player;
import java.util.Scanner;

public class CSharpCombatManager extends CombatManager {
    // Оставляем только scanner, так как он нужен для уникального текстового боя в консоли
    private Scanner scanner;

    public CSharpCombatManager(Player player, Mob mob) {
        super(player, mob); // Вызывает конструктор родителя CombatManager
        this.scanner = new Scanner(System.in);
    }

    @Override
    protected void loadChallenges() {
        // Очищаем родительский список от Java-вопросов и заполняем C# вопросами
        challenges.clear(); 
        
        challenges.add(new CodeChallenge("Как объявить целочисленную переменную x со значением 5 в C#?", "int x = 5;", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для сравнения на равенство в C#?", "==", 10));
        challenges.add(new CodeChallenge("Какой тип данных используется для хранения дробных чисел в C#?", "double", 10));
        challenges.add(new CodeChallenge("Какой цикл лучше всего использовать для итерации по массиву в C#?", "for", 10));
        challenges.add(new CodeChallenge("Как объявить список в C#?", "List<T>", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для объединения строк в C#?", "+", 10));
        challenges.add(new CodeChallenge("Какой тип данных используется для хранения символов в C#?", "char", 10));
        challenges.add(new CodeChallenge("Какой метод используется для получения длины строки в C#?", "Length", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для проверки условия в C#?", "if", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для логического И в C#?", "&&", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для логического ИЛИ в C#?", "||", 10));
        challenges.add(new CodeChallenge("Какой метод используется для вывода данных в консоль?", "Console.WriteLine()", 10));
        challenges.add(new CodeChallenge("Какой класс используется для работы с математическими функциями в C#?", "Math", 10));
        challenges.add(new CodeChallenge("Как объявить словарь в C#?", "Dictionary<TKey, TValue>", 10));
        challenges.add(new CodeChallenge("Какой метод используется для добавления элемента в список?", "Add()", 10));
        challenges.add(new CodeChallenge("Как создать метод в C#?", "void", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для проверки принадлежности к списку?", "Contains", 10));
        challenges.add(new CodeChallenge("Как получить тип переменной в C#?", "GetType()", 10));
        challenges.add(new CodeChallenge("Как преобразовать строку в число в C#?", "int.Parse()", 10));
        challenges.add(new CodeChallenge("Какой метод используется для сортировки списка?", "Sort()", 10));
        challenges.add(new CodeChallenge("Как объявить массив в C#?", "[]", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для возведения в степень?", "Math.Pow()", 10));
        challenges.add(new CodeChallenge("Как создать класс в C#?", "class", 10));
        challenges.add(new CodeChallenge("Какой метод вызывается при создании объекта?", "constructor", 10));
        challenges.add(new CodeChallenge("Как импортировать пространство имен в C#?", "using", 10));
        challenges.add(new CodeChallenge("Какой метод используется для чтения файла?", "File.ReadAllText()", 10));
        challenges.add(new CodeChallenge("Как обработать исключение в C#?", "try-catch", 10));
        challenges.add(new CodeChallenge("Как получить доступ к элементу массива по индексу?", "[]", 10));
        challenges.add(new CodeChallenge("Как создать анонимный метод в C#?", "delegate", 10));
        challenges.add(new CodeChallenge("Какой метод используется для преобразования списка в строку?", "string.Join()", 10));
        challenges.add(new CodeChallenge("Как получить ключи словаря в C#?", "Keys", 10));
        challenges.add(new CodeChallenge("Какой модификатор доступа делает поле публичным?", "public", 10));
        challenges.add(new CodeChallenge("Какой модификатор доступа делает поле приватным?", "private", 10));
        challenges.add(new CodeChallenge("Какой модификатор доступа делает поле защищенным?", "protected", 10));
        challenges.add(new CodeChallenge("Как создать интерфейс в C#?", "interface", 10));
        challenges.add(new CodeChallenge("Как реализовать интерфейс в классе?", ":", 10));
        challenges.add(new CodeChallenge("Какой метод используется для создания потока?", "Thread", 10));
        challenges.add(new CodeChallenge("Как создать свойство в C#?", "property", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для нулевого объединения?", "??", 10));
        challenges.add(new CodeChallenge("Как создать событие в C#?", "event", 10));
        challenges.add(new CodeChallenge("Какой атрибут используется для сериализации?", "[Serializable]", 10));
        challenges.add(new CodeChallenge("Как создать генерик класс в C#?", "<T>", 10));
        challenges.add(new CodeChallenge("Какой метод используется для LINQ запросов?", "LINQ", 10));
        challenges.add(new CodeChallenge("Как создать асинхронный метод в C#?", "async", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для ожидания задачи?", "await", 10));
    }

    @Override
    public void startCombat() {
        System.out.println("🖥️ БИТВА НА C# ОСТРОВЕ! Вы столкнулись с " + mob.getName());
        
        while (player.isAlive() && mob.isAlive()) {
            CodeChallenge challenge = getRandomChallenge(); // Метод унаследован от родителя
            System.out.println("\n🖥️ C# ЗАДАНИЕ: " + challenge.getQuestion());
            System.out.print("🖥️ ВАШ C# КОД: ");
            String answer = scanner.nextLine();

            if (challenge.checkAnswer(answer)) {
                System.out.println("🖥️ ВЕРНО! Вы наносите " + challenge.getDamage() + " урона.");
                mob.takeDamage(challenge.getDamage());
            } else {
                System.out.println("🖥️ ОШИБКА! " + mob.getName() + " атакует вас!");
                player.takeDamage(10);
            }

            System.out.println("🖥️ ВАШЕ HP: " + player.getHealth() + " | HP ВРАГА: " + mob.getHealth());
        }

        if (player.isAlive()) {
            System.out.println("🖥️ ПОБЕДА! Вы одолели " + mob.getName());
        } else {
            System.out.println("🖥️ ВЫ ПАЛИ В БИТВЕ...");
        }
    }
}