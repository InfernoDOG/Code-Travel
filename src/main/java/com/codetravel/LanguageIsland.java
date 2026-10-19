package com.codetravel;

public enum LanguageIsland {
    JAVA("Java", "Лес Java", "java_forest.png", "Основы программирования"),
    PYTHON("Python", "Подземелье Python", "python_dungeon.png", "Структуры данных"),
    CSHARP("C#", "Замок C#", "csharp_castle.png", "Объектно-ориентированное программирование");
    
    private final String displayName;
    private final String locationName;
    private final String backgroundFile;
    private final String topic;
    
    LanguageIsland(String displayName, String locationName, String backgroundFile, String topic) {
        this.displayName = displayName;
        this.locationName = locationName;
        this.backgroundFile = backgroundFile;
        this.topic = topic;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getLocationName() {
        return locationName;
    }
    
    public String getBackgroundFile() {
        return backgroundFile;
    }
    
    public String getTopic() {
        return topic;
    }
}
