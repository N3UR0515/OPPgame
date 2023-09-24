import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Enemy {
    private int x; // Where enemy is drawn
    private int y;
    private int rel_x; // On which tile enemy stands
    private int rel_y;
    private int HP;
    private final Polygon triangle;
    private Map map;

    public Enemy(int HP, Map map, Camera camera) {
        x = map.getTile(1).getX();
        y = map.getTile(1).getY();
        rel_x = 1;
        rel_y = 0;
        this.HP = HP;
        this.map = map;

        triangle = new Polygon();
        triangle.addPoint(x + camera.cameraX, y - 50 + camera.cameraY); // Top vertex
        triangle.addPoint(x - 50 + camera.cameraX, y + 50 + camera.cameraY); // Bottom-left vertex
        triangle.addPoint(x + 50 + camera.cameraX, y + 50 + camera.cameraY); // Bottom-right vertex
    }
    public int getRel_x(){
        return rel_x;
    }
    public int getRel_y(){
        return rel_y;
    }
    public void setRel_x(int x){
        rel_x = x;
    }
    public void setRel_y(int y){
        rel_y = y;
    }

    public void updateEnemy(Player player) {
        getRealLoc();
        triangle.setCenterX(x);
        triangle.setCenterY(y);

        if (checkDistance(player, map.getTileByLoc(rel_x, rel_y))) {
            player.damagePlayer();
        } else {
            seekPlayer(player);
        }
    }

    private void getRealLoc() {
        Tile tile = map.getTileByLoc(rel_x, rel_y);
        x = tile.getX();
        y = tile.getY();
    }

    public void drawEnemy(Graphics g) {
        g.setColor(org.newdawn.slick.Color.blue);
        g.fill(triangle);
    }

    private boolean checkDistance(Player player, Tile tile) {
        int prel_x = player.getRel_x();
        int prel_y = player.getRel_y();

//        if(tile.id %2 == 0)
//        {
//            //{-1, -1}, {0, -1}, {-1, 1}, {-1, 0}, {0, 1}, {1, 0}
//            if((prel_x == tile.getTrel_x() -1 && prel_y == tile.getTrel_y() - 1) ||
//                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() -1) ||
//                    (prel_x == tile.getTrel_x() -1 && prel_y == tile.getTrel_y() +1) ||
//                    (prel_x == tile.getTrel_x()-1 && prel_y == tile.getY()) ||
//                    (prel_x == tile.getTrel_x() && prel_y == tile.getY() + 1)||
//                    (prel_x == tile.getTrel_x()+1 && prel_y == tile.getY()))
//            {
//                return true;
//            }
//
//        }
//        else
//        {
//            //{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
//            if((prel_x == tile.getTrel_x() -1 && prel_y == tile.getTrel_y()) ||
//                    (prel_x == tile.getTrel_x()+1 && prel_y == tile.getTrel_y()) ||
//                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() -1) ||
//                    (prel_x == tile.getTrel_x() && prel_y == tile.getY()+1) ||
//                    (prel_x == tile.getTrel_x()-1 && prel_y == tile.getY() + 1)||
//                    (prel_x == tile.getTrel_x()+1 && prel_y == tile.getY()+1))
//            {
//                return true;
//            }
//        }
        return prel_x==tile.getTrel_x()&&prel_y==tile.getTrel_y();
    }

    private void seekPlayer(Player player) {
        ArrayList<Tile> closedList = new ArrayList<>();
        Queue<Tile> openQueue = new LinkedList<>();

        // Starting tile
        Tile startTile = map.getTileByLoc(rel_x, rel_y);
        openQueue.add(startTile);

        while (!openQueue.isEmpty()) {
            Tile currentTile = openQueue.poll();

            if (checkDistance(player, currentTile)) {
                // Found the player, update the enemy's position
                rel_x = currentTile.getTrel_x();
                rel_y = currentTile.getTrel_y();
                return;
            }

            closedList.add(currentTile);

            // Get neighboring tiles
            ArrayList<Tile> neighbors = getNeighbors(currentTile);

            for (Tile neighbor : neighbors) {
                if (!closedList.contains(neighbor) && !openQueue.contains(neighbor)) {
                    openQueue.add(neighbor);
                }
            }
        }
    }

    private ArrayList<Tile> getNeighbors(Tile tile) {
        ArrayList<Tile> neighbors = new ArrayList<>();

        int x = tile.getTrel_x();
        int y = tile.getTrel_y();

        int[][] directionsEven = {
                {-1, -1}, {0, -1}, {-1, 1}, {-1, 0}, {0, 1}, {1, 0}
        };
        int[][] directionsOdd = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
        };
        int[][] directions ={};

        if(tile.id %2 == 0)
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
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }
}
