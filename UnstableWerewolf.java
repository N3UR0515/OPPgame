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

    @Override
    public boolean updateCharacter(GameContainer container) {
        return false;
    }

    @Override
    public void updateCharacter(Character character) {
        getRealLoc();
        triangle.setCenterX(x);
        triangle.setCenterY(y);

        if(checkDistance((Player) character, map.getTileByLoc(rel_x, rel_y)))
        {
            character.damageCharacter();
        }
        else
        {
            seekPlayer((Player) character);
        }
    }

    @Override
    public void drawCharacter(Graphics g) {
        this.getRealLoc();
        this.triangle.setCenterX((float)this.x);
        this.triangle.setCenterY((float)this.y);
        g.setColor(Color.blue);
        g.fill(this.triangle);
    }

    @Override
    protected boolean checkDistance(Player player, Tile tile) {
        int prel_x = player.getRel_x();
        int prel_y = player.getRel_y();

        if(tile.id)
        {
            //{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}
            if((prel_x == tile.getTrel_x() -1 && prel_y == tile.getTrel_y() - 1) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() -1) ||
                    (prel_x == tile.getTrel_x() +1 && prel_y == tile.getTrel_y() -1) ||
                    (prel_x == tile.getTrel_x()-1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() + 1)||
                    (prel_x == tile.getTrel_x()+1 && prel_y == tile.getTrel_y()))
            {
                return true;
            }

        }
        else
        {
            //{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
            if((prel_x == tile.getTrel_x() -1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x()+1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() -1) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y()+1) ||
                    (prel_x == tile.getTrel_x()-1 && prel_y == tile.getTrel_y() + 1)||
                    (prel_x == tile.getTrel_x()+1 && prel_y == tile.getTrel_y()+1))
            {
                return true;
            }
        }
        return false;
        //return prel_x==tile.getTrel_x()&&prel_y==tile.getTrel_y();
    }

    @Override
    protected boolean checkDistance(Tile tile) {
        int prel_x = this.rel_x;
        int prel_y = this.rel_y;

        if(tile.id)
        {
            //{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}
            if((prel_x == tile.getTrel_x() -1 && prel_y == tile.getTrel_y() - 1) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() -1) ||
                    (prel_x == tile.getTrel_x() +1 && prel_y == tile.getTrel_y() -1) ||
                    (prel_x == tile.getTrel_x()-1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() + 1)||
                    (prel_x == tile.getTrel_x()+1 && prel_y == tile.getTrel_y()))
            {
                return true;
            }
        }
        else
        {
            //{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
            if((prel_x == tile.getTrel_x() -1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x()+1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() -1) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y()+1) ||
                    (prel_x == tile.getTrel_x()-1 && prel_y == tile.getTrel_y() + 1)||
                    (prel_x == tile.getTrel_x()+1 && prel_y == tile.getTrel_y()+1))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void seekPlayer(Player player) {
        ArrayList<Tile> closedList = new ArrayList<>();
        Queue<Tile> openQueue = new LinkedList<>();
        Tile[] prec = new Tile[map.getTileCount()];

        // Starting tile
        Tile startTile = map.getTileByLoc(rel_x, rel_y);
        openQueue.add(startTile);
        prec[startTile.getTrel_x()*map.getRows()+ startTile.getTrel_y()] = startTile;

        while (!openQueue.isEmpty()) {
            Tile currentTile = openQueue.poll();

            if (checkDistance(player, currentTile)) {
                // Found the player, update the enemy's position
//                rel_x = currentTile.getTrel_x();
//                rel_y = currentTile.getTrel_y();
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

    @Override
    protected void moveEnemyToPlayer(Tile[] prec, Tile currentTile) {
        while (prec[currentTile.getTrel_x() * map.getRows() + currentTile.getTrel_y()] != currentTile) {
            if (checkDistance(currentTile)) {
                this.rel_x = currentTile.getTrel_x();
                this.rel_y = currentTile.getTrel_y();
                break;
            }
            currentTile = prec[currentTile.getTrel_x() * map.getRows() + currentTile.getTrel_y()];
        }
    }

    @Override
    protected ArrayList<Tile> getNeighbors(Tile tile) {
        ArrayList<Tile> neighbors = new ArrayList<>();

        int x = tile.getTrel_x();
        int y = tile.getTrel_y();

        int[][] directionsEven = {
                {-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}
        };
        int[][] directionsOdd = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
        };
        int[][] directions ={};

        if(tile.id)
        {
            directions = directionsEven;
        }
        else
        {
            directions = directionsOdd;
        }
        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            if (x + dx >= 0 && x + dx < map.getCols() && y + dy >= 0 && y + dy < map.getRows()) {
                Tile neighbor = map.getTileByLoc(x + dx, y + dy);
                if (neighbor.isAvailable()){
                    neighbors.add(neighbor);
                }
            }
        }

        return neighbors;
    }
}
