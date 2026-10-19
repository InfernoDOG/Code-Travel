🛠️ Инструкция по расширению контента (Для разработчиков)

Благодаря модульной архитектуре проекта, добавление новых вопросов, монстров или целых островов происходит по чётким шаблонам без риска сломать существующую логику игры.📝 Добавление новых заданий:Все вопросы в игре завязаны на единую модель CodeChallenge. Чтобы расширить пул вопросов для конкретного языка, достаточно отредактировать менеджер боя соответствующего острова.⚙️ Процесс добавления:Откройте нужный класс в пакете com.codetravel.combat:CombatManager.java — для Java островаPythonCombatManager.java — для Python островаCSharpCombatManager.java — для C# островаНайдите метод protected void loadChallenges()Добавьте строку с новым заданием по следующему шаблону:Javachallenges.add(new CodeChallenge("ТЕКСТ_ВОПРОСА", "ПРАВИЛЬНЫЙ_ОТВЕТ", УРОН_ЗА_ОТВЕТ));

Пример кода:
    Javaprotected void loadChallenges() {
    // Старые вопросы...
    challenges.add(new CodeChallenge("Какой ключевое слово используется для создания класса в Java?", "class", 45));
    challenges.add(new CodeChallenge("Какое ключевое слово запрещает наследование класса?", "final", 75));
}

👾 Создание новых мобов и игровой баланс:Класс Mob полностью независим от графического интерфейса. Мобы могут инициализироваться как динамически в коде локаций, так и подгружаться из базы данных.

📋 Шаблоны характеристик мобов:Для сохранения правильного игрового баланса рекомендуется использовать следующие стандарты при создании существ:
Класс моба | Пример названия | Здоровье (HP) | Базовый урон игроку
Рядовой баг | Syntax Error, Missing Semicolon | 50 – 80 | 10 HP
Продвинутый баг | NullPointerException, Logic Bug | 100 – 150 | 15 HP
Босс острова | The Architect, Garbage Collector | 250 – 400 | 25 HP

💡 Пример инициализации в коде:Java// Инициализация кастомного босса для боевой сцены
Mob logicBoss = new Mob("Infinity Loop Bug", 300);
CombatManager combat = new CombatManager(player, logicBoss);

🏝️ Разработка нового тематического острова:Создание нового языка программирования в игре (например, JavaScript или C++) требует реализации полноценной структуры по паттерну MVC.

🏗️ Шаг 1: Создание логики задач (Backend)Создайте новый класс менеджера боя в пакете com.codetravel.combat, унаследовав его от базового CombatManager. Переопределите метод загрузки вопросов:Javapackage com.codetravel.combat;

import com.codetravel.entities.Mob;
import com.codetravel.entities.Player;

public class JavaScriptCombatManager extends CombatManager {
    public JavaScriptCombatManager(Player player, Mob mob) {
        super(player, mob); // Автоматически вызовет переопределенный метод loadChallenges()
    }

    @Override
    protected void loadChallenges() {
        challenges.add(new CodeChallenge("Как объявить переменную с блочной областью видимости в JS?", "let", 40));
        challenges.add(new CodeChallenge("Какой оператор проверяет равенство и тип данных?", "===", 60));
    }
}

🎨 Шаг 2: Создание интерфейса и стилей (View)FXML разметка: В папке src/main/resources/fxml/ создайте файл javascript-game-scene.fxml (за основу можно взять разметку Python-острова).CSS стилизация: В папке src/main/resources/styles/ создайте уникальные таблицы стилей для нового острова:javascript-retro-combat.css — для кастомного цвета терминала и свечения окон.javascript-shop.css — для визуального оформления магазина острова.🎮 Шаг 3: Реализация UI Контроллера (Controller)В пакете com.codetravel.ui создайте управляющий класс JavaScriptGameSceneController.java, связывающий интерфейс острова с ядром игры:Javapackage com.codetravel.ui;

import com.codetravel.core.JavaFXGame;
import com.codetravel.combat.JavaScriptCombatManager;
import com.codetravel.entities.Mob;
import javafx.fxml.FXML;

public class JavaScriptGameSceneController {
    private JavaFXGame game;

    public void setGame(JavaFXGame game) {
        this.game = game;
    }

    @FXML
    private void handleStartCombat() {
        // Запуск боя со специфичным JS-менеджером задач
        Mob jsMoneyster = new Mob("Undefined Behavior", 80);
        game.showRetroCombatScene(new JavaScriptCombatManager(game.getPlayer(), jsMoneyster));
    }
}

🎮 Шаг 3: Реализация UI Контроллера (Controller)
В пакете com.codetravel.ui создайте управляющий класс JavaScriptGameSceneController.java, связывающий интерфейс острова с ядром игры:

Java
package com.codetravel.ui;

import com.codetravel.core.JavaFXGame;
import com.codetravel.combat.JavaScriptCombatManager;
import com.codetravel.entities.Mob;
import javafx.fxml.FXML;

public class JavaScriptGameSceneController {
    private JavaFXGame game;

    public void setGame(JavaFXGame game) {
        this.game = game;
    }

    @FXML
    private void handleStartCombat() {
        // Запуск боя со специфичным JS-менеджером задач
        Mob jsMoneyster = new Mob("Undefined Behavior", 80);
        game.showRetroCombatScene(new JavaScriptCombatManager(game.getPlayer(), jsMoneyster));
    }
}

⚡ Шаг 4: Регистрация сцены в ядре игры (JavaFXGame.java)Добавьте метод для переключения сцены на новый остров в главный координирующий класс:Javapublic void showJavaScriptGameScene() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/javascript-game-scene.fxml"));
        Parent root = loader.load();
        
        JavaScriptGameSceneController controller = loader.getController();
        controller.setGame(this);
        
        scene.setRoot(root);
    } catch (IOException e) {
        System.err.println("Ошибка загрузки JavaScript сцены: " + e.getMessage());
    }
}

💾 Шаг 5: Интеграция с системой сохранений (SQLite)Убедитесь, что класс SaveManager и структура базы данных reg-log.db обновлены для поддержки нового ID или индекса острова. Это необходимо, чтобы при нажатии кнопки "ЗАГРУЗИТЬ ИГРУ" игровой процесс корректно восстанавливался на созданной локации.