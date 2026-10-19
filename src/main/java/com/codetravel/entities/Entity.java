package com.codetravel.entities;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int level;

    public Entity(String name, int maxHealth, int level) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.level = level;
    }

    public abstract void takeDamage(int damage);
    
    public boolean isAlive() {
        return health > 0;
    }

    // Getters and Setters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getLevel() { return level; }
}
