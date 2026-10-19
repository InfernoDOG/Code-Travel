package com.codetravel.levels;

import com.codetravel.entities.Mob;
import java.util.List;
import java.util.ArrayList;

public class Island {
    private String name;
    private String description;
    private List<Mob> mobs;

    public Island(String name, String description) {
        this.name = name;
        this.description = description;
        this.mobs = new ArrayList<>();
    }

    public void addMob(Mob mob) {
        mobs.add(mob);
    }

    public List<Mob> getMobs() { return mobs; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
