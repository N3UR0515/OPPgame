package AbstractFactory.Monster;

import Character.Enemies.Enemy;
import Map.Map;

public class Bomber extends Enemy {

    public Bomber(int HP, Map map, int rel_x, int rel_y) {
        super(HP, map, rel_x, rel_y);
    }
}
