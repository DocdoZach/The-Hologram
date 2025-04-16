package main.java;

import java.util.ArrayList;

public class Character extends Component {
    private int level, maxHp, curHp, atk, def, spd;

    public Character(int level, int maxHp, int atk, int def, int spd) {
        this.level = level;
        this.maxHp = maxHp;
        this.curHp = maxHp;
        this.atk = atk;
        this.def = def;
        this.spd = spd;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurHp() {
        return curHp;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getSpd() {
        return spd;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setCurHp(int curHp) {
        this.curHp = curHp;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }
}
