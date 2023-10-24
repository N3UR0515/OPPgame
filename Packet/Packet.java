package Packet;

import java.io.Serializable;

public class Packet implements Serializable {
    private int id;
    private int x;
    private int y;
    private boolean isEnemy;
    private boolean isAttack;
    private boolean setHealth;
    private int HP = -10;

    public Packet()
    {
    }

    public Packet setId(int id) {
        this.id = id;
        return this;
    }

    public Packet setX(int x) {
        this.x = x;
        return this;
    }

    public Packet setY(int y) {
        this.y = y;
        return this;
    }

    public Packet setEnemy(boolean enemy) {
        isEnemy = enemy;
        return this;
    }

    public Packet setAttack(boolean attack) {
        isAttack = attack;
        return this;
    }

    public Packet setHP(int HP) {
        this.HP = HP;
        return this;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public boolean isAttack() {
        return isAttack;
    }

    public int getHP() {
        return HP;
    }

    public boolean isSetHealth() {
        return setHealth;
    }

    public Packet setSetHealth(boolean setHealth) {
        this.setHealth = setHealth;
        return this;
    }
}
