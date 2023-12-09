package AbstractFactory.Monster;

import Character.Enemies.Enemy;
import Map.Map;
import Map.Tile.HiderTile;
import Map.Tile.Tile;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import Character.Character;
import Character.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Walker extends Enemy {
    public Walker(int HP, Map map, int rel_x, int rel_y) {
        super(HP, map, rel_x, rel_y);
    }

    @Override
    public void updateCharacter(Character character) {
        getRealLoc();
        triangle.setCenterX(x);
        triangle.setCenterY(y);

       seekTile();
    }

    @Override
    public void drawCharacter(Graphics g, int x, int y, int HP) {
        if (map.getTileByLoc(rel_x, rel_y).getClass() != HiderTile.class) {
            this.getRealLoc();
            this.triangle.setCenterX((float)this.x);
            this.triangle.setCenterY((float)this.y);
            g.setColor(Color.blue);
            g.fill(this.triangle);
        }
    }

    public void seekTile()
    {
        ArrayList<Tile> closedList = new ArrayList<>();
        Queue<Tile> openQueue = new LinkedList<>();
        Tile[] prec = new Tile[map.getTileCount()];

        // Starting tile
        Tile startTile = map.getTileByLoc(rel_x, rel_y);
        openQueue.add(startTile);
        prec[startTile.getTrel_x()*map.getRows()+ startTile.getTrel_y()] = startTile;

        while (!openQueue.isEmpty()) {
            Tile currentTile = openQueue.poll();

            if (currentTile.getPickUp() != null) {
                // Found the player, update the enemy's position
                moveEnemyToPlayer(prec, currentTile);
                return;
            }

            closedList.add(currentTile);

            // Get neighboring tiles
            ArrayList<Tile> neighbors = getNeighbors(currentTile);

            for (Tile neighbor : neighbors) {
                if (!closedList.contains(neighbor) && !openQueue.contains(neighbor)) {
                    openQueue.add(neighbor);
                    prec[neighbor.getTrel_x() * map.getRows() + neighbor.getTrel_y()] =  currentTile;
                }
            }
        }
    }
}
