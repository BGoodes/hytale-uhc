package fr.bgoodes.uhc.context;

public class GameRules {

    private boolean pvp;
    private boolean building;
    private boolean invincible;

    public boolean pvp() {
        return pvp;
    }

    public void pvp(boolean value) {
        this.pvp = value;
    }

    public boolean building() {
        return building;
    }

    public void building(boolean value) {
        this.building = value;
    }

    public boolean invincible() {
        return invincible;
    }

    public void invincible(boolean value) {
        this.invincible = value;
    }
}
