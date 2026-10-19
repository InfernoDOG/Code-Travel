package com.codetravel.entities;

public class Mob extends Entity {
    public enum Type { NORMAL, MINI_BOSS, BOSS }
    
    private String description;
    private Type type;
    private int expReward;

    public Mob(String name, int health, int level, String description, Type type) {
        super(name, health, level);
        this.description = description;
        this.type = type;
        this.expReward = level * (type == Type.NORMAL ? 10 : (type == Type.MINI_BOSS ? 50 : 200));
    }

    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) this.health = 0;
    }

    public Type getType() { return type; }
    public int getExpReward() { return expReward; }
    public String getDescription() { return description; }
}
