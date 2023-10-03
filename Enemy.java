import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Enemy extends Character {

    public Enemy(int HP, Map map, int rel_x, int rel_y, Camera camera)
    {
        super(HP, map, rel_x, rel_y, camera);
    }
    public Enemy(int HP, Map map, int rel_x, int rel_y)
    {
        super(HP, map, rel_x, rel_y);
    }

    protected abstract boolean checkDistance(Player player, Tile tile);

    protected abstract boolean checkDistance(Tile tile);

    protected abstract void seekPlayer(Player player);
    protected abstract void moveEnemyToPlayer(Tile[] prec, Tile currentTile);

    protected abstract ArrayList<Tile> getNeighbors(Tile tile);
}
