package Main;

public class ResilientWerewolf extends Enemy {
    public ResilientWerewolf(int HP, Map map, int rel_x, int rel_y, Camera camera) {
        super(HP, map, rel_x, rel_y, camera);
    }

    public ResilientWerewolf(int HP, Map map, int rel_x, int rel_y) {
        super(HP, map, rel_x, rel_y);
    }
}
