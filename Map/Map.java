package Map;

import Map.Tile.*;
import Character.Camera;
import Visitor.*;
import PickUp.PickUpStore;
import org.newdawn.slick.Graphics;
import java.util.*;
import java.io.Serializable;

public class Map implements Serializable {
    protected final Tile[] tiles;
    protected final int cols;
    protected final int rows;
    protected final TileFactory factory;

    protected Map(Tile[] tiles, int cols, int rows)
    {
        this.tiles = tiles;
        this.cols = cols;
        this.rows = rows;
        factory = new TileFactory();
    }
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
        tiles[0] =  factory.createTile(0, true, 0, 0, "");
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
                tiles[tempCol * this.rows + tempRow] = factory.createTile(0, true, tempRow, tempCol, "");
            } else {
                if (tiles[tempCol * rows + tempRow] != null) {
                    i--;
                    continue;
                }
                coordSave.add(tempCol + "_" + tempRow);
                tiles[tempCol * this.rows + tempRow] = factory.createTile(0, false, tempRow, tempCol, "");
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
                    tiles[col * this.rows + row] = factory.createTile(-1, true, row, col, "");
                }
                else
                {
                    if (tiles[col * rows + row] != null) {
                        continue;
                    }
                    tiles[col * this.rows + row] = factory.createTile(-1, false, row, col, "");
                }

            }
        }

        //Generating paths
        Visitor bfsVisitor = new BFSVisitor();
        Visitor backVisitor  = new BacktrackVisitor();
        Tile[] prec;
        while (coordSave.size() != 0)  {
            String StartPoint = coordSave.remove(0);
            if (coordSave.size() > 5) {
                for (int i = 0; i < 2; i++) {
                    int chosenIndex = rand.nextInt(coordSave.size());
                    prec = this.accept(bfsVisitor, StartPoint, coordSave.get(chosenIndex));
                    Tile current = prec[prec.length-1];
                    this.accept(backVisitor, prec, current, rand.nextInt(4)==3, rand);
                }
            } else if (coordSave.size() <= 0) {
                break;
            } else {
                prec = this.accept(bfsVisitor, StartPoint, coordSave.get(0));
                Tile current = prec[prec.length-1];
                this.accept(backVisitor, prec, current, rand.nextInt(4)==3, rand);
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
                tiles[tile.getTrel_x() * this.rows + tile.getTrel_y()] = factory.createTile(0, tile.id, tile.getTrel_y(), tile.getTrel_x(), "");
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
                    tiles[tempCol * this.rows + tempRow] = factory.createTile(1, true, tempRow, tempCol, "");
                }
            } else {
                if (tiles[tempCol * rows + tempRow].getClass() != RegularTile.class) {
                    i--;
                    continue;
                } else {
                    tiles[tempCol * this.rows + tempRow] = factory.createTile(1, false, tempRow, tempCol, "");
                }
            }
        }

        //Generating fiery lines
        this.generateFieryLines();
    }

    public List<Tile> generateHealthTiles()
    {
        List<Tile> tilesWithPickups = new ArrayList<>();
        getTileByLoc(0, 1).setPickUp(PickUpStore.getPickUp("Heal"));
        tilesWithPickups.add(getTileByLoc(0, 1));
        return tilesWithPickups;
    }

    protected void generateFieryLines() {
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
                        tiles[tempCol * this.rows + tempRow] = factory.createTile(2, tiles[tempCol * this.rows + tempRow].id, tempRow, tempCol, "");
                    }
                } else {
                    break;
                }
            }
        }
    }

    protected int decryptCol(String coordinate){
        int split = coordinate.indexOf("_");
        return Integer.parseInt(coordinate.substring(0, split));
    }

    protected int decryptRow(String coordinate){
        int split = coordinate.indexOf("_");
        return Integer.parseInt(coordinate.substring(split+1));
    }

    //Not to be confused with "viewport" areas. For this method, area means getting tiles around one specific tile
    protected void getAreaTiles(ArrayList<Tile> list, int depth, Tile currentTile) {
        Visitor visitor = new NeighboringTilesVisitor();
        ArrayList<Tile> temp = this.accept(visitor, currentTile);
        list.addAll(temp);
        if (depth > 0) {
            for (Tile t : temp){
                getAreaTiles(list, depth - 1, t);
            }
        }
    }

    public boolean isUnavailable(Tile tile) {
        return tile.getClass()== UnavailableTile.class;
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

    public void replaceTile(int x, int y, Tile newTile) {tiles[x * rows + y] = newTile;}

    public int getTileCount(){
        return this.tiles.length;
    }

    public Map copy()
    {
        Tile[] tilesCopy = new Tile[cols * rows];
        for(int i = 0; i < cols*rows; i++)
        {
            tilesCopy[i] = tiles[i].copy();
        }
        return new Map(tilesCopy, cols, rows);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Map map = (Map) o;
        return cols == map.cols && rows == map.rows;
    }

    private Tile[] accept(Visitor visitor, String from, String to) {
        Tile startTile = this.getTileByLoc(decryptCol(from), decryptRow(from));
        Tile tileToFind = this.getTileByLoc(decryptCol(to), decryptRow(to));
        return visitor.visit(this, startTile, tileToFind);
    }

    private void accept(Visitor visitor, Tile[] prec, Tile currentTile, boolean isWide, Random rng){
        visitor.visit(this, prec, currentTile, isWide, rng, this.factory);
    }

    private ArrayList<Tile> accept(Visitor visitor, Tile tile){
        return visitor.visit(this, tile);
    }

    public void setTile(int index, Tile tile){
        tiles[index] = tile;
    }

}
