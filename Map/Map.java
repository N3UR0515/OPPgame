package Map;

import Map.Tile.*;
import Character.Camera;
import org.newdawn.slick.Graphics;
import java.util.*;
import java.io.Serializable;

public class Map implements Serializable {
    private final Tile[] tiles;
    private final int cols;
    private final int rows;
    private final TileFactory factory;
    public Map(int cols, int rows)
    {
        factory = new TileFactory();
        int initTileCount = 109, hiderTileCount = 65;
        this.cols = cols;
        this.rows = rows;
        tiles = new Tile[cols * rows];
        ArrayList<String> coordSave = new ArrayList<>();
        ArrayList<String>  roomCenters = new ArrayList<>();
        Random rand = new Random();

        //Setting 0;0 tile as starter one
        tiles[0] =  factory.createTile(0, true, 0, 0);
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
                tiles[tempCol * this.rows + tempRow] = factory.createTile(0, true, tempRow, tempCol);
            } else {
                if (tiles[tempCol * rows + tempRow] != null) {
                    i--;
                    continue;
                }
                coordSave.add(tempCol + "_" + tempRow);
                tiles[tempCol * this.rows + tempRow] = factory.createTile(0, false, tempRow, tempCol);
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
                    tiles[col * this.rows + row] = factory.createTile(-1, true, row, col);
                }
                else
                {
                    if (tiles[col * rows + row] != null) {
                        continue;
                    }
                    tiles[col * this.rows + row] = factory.createTile(-1, false, row, col);
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

        //Getting tiles for rooms
        ArrayList<Tile> roomTiles = new ArrayList<>(); //After all getAreaTiles() calls this will be filled with room tiles. Duplicates will be here
        for (String center : roomCenters) {
            getAreaTiles(roomTiles, 4, getTileByLoc(decryptCol(center), decryptRow(center)));
        }

        //Removing duplicates from room tiles
        Set<Tile> set = new LinkedHashSet<>(roomTiles);
        roomTiles.clear();
        roomTiles.addAll(set);

        //Generating rooms
        for (Tile tile : roomTiles){
            if (isUnavailable(tile)) {
                tiles[tile.getTrel_x() * this.rows + tile.getTrel_y()] = factory.createTile(0, tile.id, tile.getTrel_y(), tile.getTrel_x());
            }
        }

        //Generating hiding tiles
        for (int i = 0; i < hiderTileCount; i++){
            int tempRow = rand.nextInt(rows);
            int tempCol = rand.nextInt(cols);
            if (tempCol % 2 == 0){
                if (tiles[tempCol * rows + tempRow].getClass() != RegularTile.class) {
                    i--;
                    continue;
                } else {
                    tiles[tempCol * this.rows + tempRow] = factory.createTile(1, true, tempRow, tempCol);
                }
            } else {
                if (tiles[tempCol * rows + tempRow].getClass() != RegularTile.class) {
                    i--;
                    continue;
                } else {
                    tiles[tempCol * this.rows + tempRow] = factory.createTile(1, false, tempRow, tempCol);
                }
            }
        }

        //Generating fiery lines
        this.generateFieryLines();

    }

    private void generateFieryLines() {
        int fieryLineCount = 25;
        Random rand = new Random();
        int[][] directionsId = {
                {-1, -1}, {0, -1}, {1, -1}, {1, 0}, {0, 1}, {-1, 0}
        };
        int[][] directionsNotId = {
                {-1, 0}, {0, -1}, {1, 0}, {-1, 1}, {1, 1} , {0, 1}
        };
        for (int i = 0; i < fieryLineCount; i++){
            int tempRow = rand.nextInt(rows);
            int tempCol = rand.nextInt(cols);
            int length = 4 + rand.nextInt(17);
            int direction = rand.nextInt(6);
            for (int j = 0; j < length; j++){
                //decide whether to curve line
                int toCurve = rand.nextInt(11);
                if (toCurve == 0) {
                    if (direction == 0) {
                        direction = 5;
                    } else {
                        direction--;
                    }
                } else if (toCurve == 10) {
                    if (direction == 5) {
                        direction = 0;
                    } else {
                        direction++;
                    }
                }

                //make tile fiery
                if (tiles[tempCol * this.rows + tempRow].id) {
                    tempCol += directionsId[direction][0];
                    tempRow += directionsId[direction][1];
                } else {
                    tempCol += directionsNotId[direction][0];
                    tempRow += directionsNotId[direction][1];
                }
                if (tempCol >= 0 && tempCol < this.cols && tempRow >= 0 && tempRow < this.rows){
                    if (!isUnavailable(tiles[tempCol * this.rows + tempRow])){
                        tiles[tempCol * this.rows + tempRow] = factory.createTile(2, tiles[tempCol * this.rows + tempRow].id, tempRow, tempCol);
                    }
                } else {
                    break;
                }
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
                paintPath(prec, currentTile, decisionMaker == 3, RNG);
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

    //Not to be confused with "viewport" areas. For this method, area means getting tiles around one specific tile
    private void getAreaTiles(ArrayList<Tile> list, int depth, Tile currentTile) {
        ArrayList<Tile> temp = getNeighbors(currentTile);
        list.addAll(temp);
        if (depth > 0) {
            for (Tile t : temp){
                getAreaTiles(list, depth - 1, t);
            }
        }
    }

    private void paintPath(Tile[] prec, Tile currentTile, boolean isWide, Random rng) {
        boolean isFiery = isWide && rng.nextInt(6) == 5;
        while (prec[currentTile.getTrel_x() * this.rows + currentTile.getTrel_y()] != currentTile) {
            if (isWide) {
                ArrayList<Tile> neighbors = getNeighbors(currentTile);
                for (Tile n : neighbors){
                    if (isUnavailable(n)) {
                        if (isFiery && rng.nextInt(16) == 4) {
                            tiles[n.getTrel_x() * this.rows + n.getTrel_y()] = factory.createTile(2, n.id, n.getTrel_y(), n.getTrel_x());
                        } else {
                            tiles[n.getTrel_x() * this.rows + n.getTrel_y()] = factory.createTile(0, n.id, n.getTrel_y(), n.getTrel_x());
                        }
                    }
                }
            }
            if (isUnavailable(currentTile)) {
                if (isFiery && rng.nextInt(16) == 4){
                    tiles[currentTile.getTrel_x() * this.rows + currentTile.getTrel_y()] = factory.createTile(2, currentTile.id, currentTile.getTrel_y(), currentTile.getTrel_x());
                } else {
                    tiles[currentTile.getTrel_x() * this.rows + currentTile.getTrel_y()] = factory.createTile(0, currentTile.id, currentTile.getTrel_y(), currentTile.getTrel_x());
                }
            }
            currentTile = prec[currentTile.getTrel_x() * this.rows + currentTile.getTrel_y()];
        }
    }

    private boolean isUnavailable(Tile tile) {
        return tile.getClass() == UnavailableTile.class;
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
            if (tile.getClass() != UnavailableTile.class){
                tile.setX(tile.getX() + camera.getCameraX());
                tile.setY(tile.getY() + camera.getCameraY());
                tile.draw(g);
            }
        }
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

    public int getTileCount(){
        return this.tiles.length;
    }
}
