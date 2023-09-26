import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.*;

public class Map {
    private Tile[] tiles;
    private int cols;
    private int rows;
    public Map(int cols, int rows)
    {
        int initTileCount = 109;
        this.cols = cols;
        this.rows = rows;
        tiles = new Tile[cols * rows];
        ArrayList<String> coordSave = new ArrayList<>();
        ArrayList<String>  roomCenters = new ArrayList<>();
        Random rand = new Random();

        //Setting 0;0 tile as starter one
        tiles[0] = new Tile(true, Tile.getSize(), Tile.getSize(), 0, 0, Color.white, true, true);
        roomCenters.add("0_0");
        coordSave.add("0_0");

        //Picking some tiles as nodes for map generation
        for (int i = 0; i < initTileCount; i++)
        {
            int tempRow = rand.nextInt(rows);
            int tempCol = rand.nextInt(cols);
            if (tempCol % 2 == 0) {
                if (tiles[tempCol * rows + tempRow] != null) {
                    i--;
                    continue;
                }
                coordSave.add(tempCol + "_" + tempRow);
                createTile(tempRow, tempCol, true, true);
            } else {
                if (tiles[tempCol * rows + tempRow] != null) {
                    i--;
                    continue;
                }
                coordSave.add(tempCol + "_" + tempRow);
                createTile(tempRow, tempCol, true, false);
            }
        }

        //Initializing all uninitialized tiles
        for(int col = 0; col < cols; col++)
        {
            for(int row = 0; row < rows; row++)
            {
                if(col % 2 == 0)
                {
                    if (tiles[col * rows + row] != null) {
                        continue;
                    }
                    createTile(row, col, false, true);
                }
                else
                {
                    if (tiles[col * rows + row] != null) {
                        continue;
                    }
                    createTile(row, col, false, false);
                }

            }
        }

        //Generating paths
        while (coordSave.size() != 0)  {
            String StartPoint = coordSave.remove(0);
            if (coordSave.size() > 5) {
                for (int i = 0; i < 2; i++) {
                    int chosenIndex = rand.nextInt(coordSave.size());
                    BFS(StartPoint, coordSave.get(chosenIndex));
                }
            } else if (coordSave.size() <= 0) {
                break;
            } else {
                BFS(StartPoint, coordSave.get(0));
            }
        }

        //Generating rooms
        ArrayList<Tile> roomTiles = new ArrayList<>(); //After all getAreaTiles() calls this will be filled with room tiles. Duplicates will be here
        for (String center : roomCenters) {
            getAreaTiles(roomTiles, 2, getTileByLoc(decryptCol(center), decryptRow(center)));
        }
        //Removing duplicates
        Set<Tile> set = new LinkedHashSet<>(roomTiles);
        roomTiles.clear();
        roomTiles.addAll(set);

        for (Tile tile : roomTiles){
            if (isUnavailable(tile)) {
                createTile(tile.getTrel_y(), tile.getTrel_x(), true, tile.id);
            }
        }
    }

    private void BFS(String start, String end){
        ArrayList<Tile> closedList = new ArrayList<>();
        Queue<Tile> openQueue = new LinkedList<>();
        Tile[] prec = new Tile[this.tiles.length];

        // Starting tile followed by destination tile
        Tile startTile = this.getTileByLoc(decryptCol(start), decryptRow(start));
        Tile tileToFind = this.getTileByLoc(decryptCol(end), decryptRow(end));
        openQueue.add(startTile);
        prec[startTile.getTrel_x() * this.rows + startTile.getTrel_y()] = startTile;

        while (!openQueue.isEmpty()) {
            Tile currentTile = openQueue.poll();

            if (isCloseToTile(tileToFind, currentTile)) {
                Random RNG = new Random();
                int decisionMaker = RNG.nextInt(4);
                paintPath(prec, currentTile, decisionMaker == 3);
                return;
            }

            closedList.add(currentTile);

            // Get neighboring tiles
            ArrayList<Tile> neighbors = getNeighbors(currentTile);

            for (Tile neighbor : neighbors) {
                if (!closedList.contains(neighbor) && !openQueue.contains(neighbor)) {
                    openQueue.add(neighbor);
                    prec[neighbor.getTrel_x() * this.rows + neighbor.getTrel_y()] = currentTile;
                }
            }
        }
    }

    private int decryptCol(String coordinate){
        int split = coordinate.indexOf("_");
        return Integer.parseInt(coordinate.substring(0, split));
    }

    private int decryptRow(String coordinate){
        int split = coordinate.indexOf("_");
        return Integer.parseInt(coordinate.substring(split+1));
    }

    private ArrayList<Tile> getNeighbors(Tile tile) {
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

            if (x + dx >= 0 && x + dx < this.getCols() && y + dy >= 0 && y + dy < this.getRows()) {
                Tile neighbor = this.getTileByLoc(x + dx, y + dy);
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private void getAreaTiles(ArrayList<Tile> list, int depth, Tile currentTile) {
        ArrayList<Tile> temp = getNeighbors(currentTile);
        list.addAll(temp);
        if (depth > 0) {
            for (Tile t : temp){
                getAreaTiles(list, depth - 1, t);
            }
        }
    }

    private void paintPath(Tile[] prec, Tile currentTile, boolean isWide) {
        while (prec[currentTile.getTrel_x() * this.rows + currentTile.getTrel_y()] != currentTile) {
            if (isWide) {
                ArrayList<Tile> neighbors = getNeighbors(currentTile);
                for (Tile n : neighbors){
                    if (isUnavailable(n)) {
                        createTile(n.getTrel_y(), n.getTrel_x(),true, n.id);
                    }
                }
            }
            if (isUnavailable(currentTile)) {
                createTile(currentTile.getTrel_y(), currentTile.getTrel_x(), true, currentTile.id);
            }
            currentTile = prec[currentTile.getTrel_x() * this.rows + currentTile.getTrel_y()];
        }
    }

    private boolean isUnavailable(Tile tile) {
        return !tile.isAvailable();
    }

    private boolean isCloseToTile(Tile destination, Tile currentTile) {
        int destX = destination.getTrel_x();
        int destY = destination.getTrel_y();

        if(currentTile.id)
        {
            //{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}
            if((destX == currentTile.getTrel_x() -1 && destY == currentTile.getTrel_y() - 1) ||
                    (destX == currentTile.getTrel_x() && destY == currentTile.getTrel_y() -1) ||
                    (destX == currentTile.getTrel_x() +1 && destY == currentTile.getTrel_y() -1) ||
                    (destX == currentTile.getTrel_x()-1 && destY == currentTile.getY()) ||
                    (destX == currentTile.getTrel_x() && destY == currentTile.getY() + 1)||
                    (destX == currentTile.getTrel_x()+1 && destY == currentTile.getY()))
            {
                return true;
            }
        }
        else
        {
            //{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
            if((destX == currentTile.getTrel_x() -1 && destY == currentTile.getTrel_y()) ||
                    (destX == currentTile.getTrel_x()+1 && destY == currentTile.getTrel_y()) ||
                    (destX == currentTile.getTrel_x() && destY == currentTile.getTrel_y() -1) ||
                    (destX == currentTile.getTrel_x() && destY == currentTile.getTrel_y()+1) ||
                    (destX == currentTile.getTrel_x()-1 && destY == currentTile.getTrel_y() + 1)||
                    (destX == currentTile.getTrel_x()+1 && destY == currentTile.getTrel_y()+1))
            {
                return true;
            }
        }
        return false;
    }

    public void drawMap(Graphics g, Camera camera) {
        for (Tile tile : tiles) {
            if (tile.isAvailable()){
                tile.setX(tile.getX() + camera.cameraX);
                tile.setY(tile.getY() + camera.cameraY);
                tile.draw(g);
            }
        }
    }

    private void createTile(int row, int column, boolean isWhite, boolean isID) {
        Color color;
        if (isWhite) {
            color = Color.white;
        } else {
            color = Color.black;
        }
        tiles[column * this.rows + row] =
                new Tile(isID,Tile.getSize() + column * Tile.getSize() + (int)(Tile.getSize()/1.5) * column,
                        isID? Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row : 2 * Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row,
                        column, row,
                        color,
                        true,
                        isWhite);
    }

    public int getRows()
    {
        return rows;
    }

    public int getCols()
    {
        return cols;
    }

    public Tile getTile(int index)
    {
        return tiles[index];
    }

    public Tile getTileByLoc(int x, int y)
    {
        return tiles[x * rows + y];
    }

}
