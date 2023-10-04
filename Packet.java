import java.io.Serializable;

public class Packet implements Serializable {
    public int id;
    public int x;
    public int y;
    public boolean isEnemy;
    public boolean isAttack;
    public int HP = -10;

    public Packet(int id, int x, int y, boolean isEnemy)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.isEnemy = isEnemy;
    }
}
