import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class UnstableWerewolf extends Enemy {
    public UnstableWerewolf(int HP, Map map, int rel_x, int rel_y, Camera camera) {
        super(HP, map, rel_x, rel_y, camera);
    }

    public UnstableWerewolf(int HP, Map map, int rel_x, int rel_y) {
        super(HP, map, rel_x, rel_y);
    }
}
