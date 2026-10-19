package com.codetravel.entities;

/**
 * Перечисление типов островов с их визуальными настройками
 * Используется для динамической загрузки тем оформления и управления островами
 */
public enum IslandType {
    JAVA("Java Остров", "/styles/java-game-scene.css", "/styles/retro-combat.css", "/styles/shop.css", "brown"),
    PYTHON("Python Остров", "/styles/python-retro-combat.css", "/styles/python-retro-combat.css", "/styles/python-shop.css", "green"),
    CSHARP("C# Остров", "/styles/csharp-retro-combat.css", "/styles/csharp-retro-combat.css", "/styles/csharp-shop.css", "purple");

    private final String displayName;
    private final String gameSceneCssPath;
    private final String combatCssPath;
    private final String shopCssPath;
    private final String themeColor;

    IslandType(String displayName, String gameSceneCssPath, String combatCssPath, String shopCssPath, String themeColor) {
        this.displayName = displayName;
        this.gameSceneCssPath = gameSceneCssPath;
        this.combatCssPath = combatCssPath;
        this.shopCssPath = shopCssPath;
        this.themeColor = themeColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGameSceneCssPath() {
        return gameSceneCssPath;
    }

    public String getCombatCssPath() {
        return combatCssPath;
    }

    public String getShopCssPath() {
        return shopCssPath;
    }

    public String getThemeColor() {
        return themeColor;
    }

    /**
     * Получает IslandType по индексу (для совместимости с существующей системой)
     */
    public static IslandType fromIndex(int index) {
        switch (index) {
            case 1:
                return PYTHON;
            case 2:
                return CSHARP;
            default:
                return JAVA;
        }
    }

    /**
     * Получает индекс острова (для совместимости с существующей системой)
     */
    public int getIndex() {
        switch (this) {
            case PYTHON:
                return 1;
            case CSHARP:
                return 2;
            default:
                return 0;
        }
    }
}
