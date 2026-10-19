package com.codetravel.combat;

import com.codetravel.entities.Mob;
import com.codetravel.entities.Player;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class CombatManager {
    // Изменили на protected, чтобы наследники видели эти поля
    protected Player player;
    protected Mob mob;
    protected List<CodeChallenge> challenges;
    private Scanner scanner;

    public CombatManager(Player player, Mob mob) {
        this.player = player;
        this.mob = mob;
        this.challenges = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        loadChallenges(); // Благодаря полиморфизму Java вызовет переопределенный метод у наследника!
    }

    public List<CodeChallenge> getTasks() {
    return this.challenges; 
    }
    
    // Изменили на protected, чтобы метод можно было переопределить в Python/CSharp менеджерах
    protected void loadChallenges() {
        // Адаптивный список заданий для Java острова
        challenges.add(new CodeChallenge("Как объявить целочисленную переменную x со значением 5 в Java?", "int x = 5;", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для сравнения на равенство?", "==", 10));
        challenges.add(new CodeChallenge("Как называется метод, который является точкой входа в Java программу?", "main", 10));
        challenges.add(new CodeChallenge("Какой тип данных используется для хранения дробных чисел в Java?", "double", 10));
        challenges.add(new CodeChallenge("Какой цикл лучше всего использовать для итерации по массиву?", "for", 10));
        challenges.add(new CodeChallenge("Какой модификатор доступа делает поле приватным?", "private", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для объединения строк в Java?", "+", 10));
        challenges.add(new CodeChallenge("Какой тип данных используется для хранения символов в Java?", "char", 10));
        challenges.add(new CodeChallenge("Какой метод используется для получения длины строки в Java?", "length()", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для проверки условия в Java?", "if", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для логического И в Java?", "&&", 10));
        challenges.add(new CodeChallenge("Какой оператор используется для логического ИЛИ в Java?", "||", 10));
        challenges.add(new CodeChallenge("Какой метод используется для вывода данных в консоль?", "System.out.println()", 10));
        challenges.add(new CodeChallenge("Какой класс используется для работы с математическими функциями в Java?", "Math", 10));
    }

    public CodeChallenge getRandomChallenge() {
        return challenges.get((int)(Math.random() * challenges.size()));
    }

    public void startCombat() {
        System.out.println("БИТВА НАЧАЛАСЬ! Вы столкнулись с " + mob.getName());
        
        while (player.isAlive() && mob.isAlive()) {
            CodeChallenge challenge = getRandomChallenge();
            System.out.println("\nЗАДАНИЕ: " + challenge.getQuestion());
            System.out.print("ВАШ КОД: ");
            String answer = scanner.nextLine();

            if (challenge.checkAnswer(answer)) {
                System.out.println("ВЕРНО! Вы наносите " + challenge.getDamage() + " урона.");
                mob.takeDamage(challenge.getDamage());
            } else {
                System.out.println("ОШИБКА! " + mob.getName() + " атакует вас!");
                player.takeDamage(10);
            }

            System.out.println("ВАШЕ HP: " + player.getHealth() + " | HP ВРАГА: " + mob.getHealth());
        }

        if (player.isAlive()) {
            System.out.println("ПОБЕДА! Вы одолели " + mob.getName());
        } else {
            System.out.println("ВЫ ПАЛИ В БИТВЕ...");
        }
    }
}