package Main;

public class InfectiousZombie extends Enemy {
    public InfectiousZombie(int HP, Map map, int rel_x, int rel_y, Camera camera) {
        super(HP, map, rel_x, rel_y, camera);
    }

    public InfectiousZombie(int HP, Map map, int rel_x, int rel_y) {
        super(HP, map, rel_x, rel_y);
    }
}
