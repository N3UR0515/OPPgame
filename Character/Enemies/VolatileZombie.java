package Character.Enemies;

import Character.Camera;

import Map.Map;

public class VolatileZombie extends Enemy {
    public VolatileZombie(int HP, Map map, int rel_x, int rel_y, Camera camera) {
        super(HP, map, rel_x, rel_y, camera);
    }

    public VolatileZombie(int HP, Map map, int rel_x, int rel_y) {
        super(HP, map, rel_x, rel_y);
    }
}
