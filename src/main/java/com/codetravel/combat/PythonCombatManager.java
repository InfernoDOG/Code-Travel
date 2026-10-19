package com.codetravel.combat;

import com.codetravel.entities.Mob;
import com.codetravel.entities.Player;
import java.util.Scanner;

public class PythonCombatManager extends CombatManager {
    private Scanner scanner;

    public PythonCombatManager(Player player, Mob mob) {
        super(player, mob); // Вызывает конструктор родителя CombatManager
        this.scanner = new Scanner(System.in);
    }

    @Override
    protected void loadChallenges() {
        // Очищаем родительский список от Java-вопросов и заполняем Python вопросами
        challenges.clear();
        
        challenges.add(new CodeChallenge("Как объявить переменную x со значением 5 в Python?", "x = 5", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для сравнения на равенство в Python?", "==", 10));
        challenges.add(new CodeChallenge("Какой тип данных используется для хранения дробных чисел в Python?", "float", 45));
        challenges.add(new CodeChallenge("Какой цикл лучше всего использовать для итерации по списку?", "for", 10));
        challenges.add(new CodeChallenge("Как объявить список в Python?", "[]", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для объединения строк в Python?", "+", 10));
        challenges.add(new CodeChallenge("Какой тип данных используется для хранения символов в Python?", "str", 10));
        challenges.add(new CodeChallenge("Какой метод используется для получения длины строки в Python?", "len()", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для проверки условия в Python?", "if", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для логического И в Python?", "and", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для логического ИЛИ в Python?", "or", 10));
        challenges.add(new CodeChallenge("Какой метод используется для вывода данных в консоль?", "print()", 10));
        challenges.add(new CodeChallenge("Какой модуль используется для работы с математическими функциями в Python?", "math", 10));
        challenges.add(new CodeChallenge("Как объявить словарь в Python?", "{}", 10));
        challenges.add(new CodeChallenge("Какой метод используется для добавления элемента в список?", "append()", 10));
        challenges.add(new CodeChallenge("Как создать функцию в Python?", "def", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для проверки принадлежности к списку?", "in", 10));
        challenges.add(new CodeChallenge("Как получить тип переменной в Python?", "type()", 10));
        challenges.add(new CodeChallenge("Как преобразовать строку в число в Python?", "int()", 10));
        challenges.add(new CodeChallenge("Какой метод используется для сортировки списка?", "sort()", 10));
        challenges.add(new CodeChallenge("Как объявить кортеж в Python?", "()", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для возведения в степень?", "**", 10));
        challenges.add(new CodeChallenge("Как создать класс в Python?", "class", 10));
        challenges.add(new CodeChallenge("Какой метод вызывается при создании объекта?", "__init__", 10));
        challenges.add(new CodeChallenge("Как импортировать модуль в Python?", "import", 10));
        challenges.add(new CodeChallenge("Какой метод используется для чтения файла?", "read()", 10));
        challenges.add(new CodeChallenge("Как обработать исключение в Python?", "try", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для срезов списка?", ":", 10));
        challenges.add(new CodeChallenge("Как создать lambda функцию в Python?", "lambda", 10));
        challenges.add(new CodeChallenge("Какой метод используется для преобразования списка в строку?", "join()", 10));
        challenges.add(new CodeChallenge("Как получить ключи словаря в Python?", "keys()", 10));
    }

    @Override
    public void startCombat() {
        System.out.println("🐍 БИТВА НА PYTHON ОСТРОВЕ! Вы столкнулись с " + mob.getName());
        
        while (player.isAlive() && mob.isAlive()) {
            CodeChallenge challenge = getRandomChallenge(); // Метод унаследован от родителя
            System.out.println("\n🐍 PYTHON ЗАДАНИЕ: " + challenge.getQuestion());
            System.out.print("🐍 ВАШ PYTHON КОД: ");
            String answer = scanner.nextLine();

            if (challenge.checkAnswer(answer)) {
                System.out.println("🐍 ВЕРНО! Вы наносите " + challenge.getDamage() + " урона.");
                mob.takeDamage(challenge.getDamage());
            } else {
                System.out.println("🐍 ОШИБКА! " + mob.getName() + " атакует вас!");
                player.takeDamage(10);
            }

            System.out.println("🐍 ВАШЕ HP: " + player.getHealth() + " | HP ВРАГА: " + mob.getHealth());
        }

        if (player.isAlive()) {
            System.out.println("🐍 ПОБЕДА! Вы одолели " + mob.getName());
        } else {
            System.out.println("🐍 ВЫ ПАЛИ В БИТВЕ...");
        }
    }
}