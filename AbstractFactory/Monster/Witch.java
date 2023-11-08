package AbstractFactory.Monster;

import Character.Enemies.Enemy;
import Map.Map;

public class Witch extends Enemy {
    public Witch(int HP, Map map, int rel_x, int rel_y) {
        super(HP, map, rel_x, rel_y);
    }
}
