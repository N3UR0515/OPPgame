import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.io.Serializable;

public class Map implements Serializable {
    private Tile[] tiles;
    private int cols;
    private int rows;
    public Map(int cols, int rows)
    {
        this.cols = cols;
        this.rows = rows;
        tiles = new Tile[cols * rows];
        for(int col = 0; col < cols; col++)
        {
            for(int row = 0; row < rows; row++)
            {
                if(col % 2 == 0)
                {
                    tiles[col * rows + row] =
                            new Tile(row,Tile.getSize() + col * Tile.getSize() + (int)(Tile.getSize()/1.5) * col,
                                    Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row,
                                    col, row,
                                    Color.white,
                                    true,
                                    true);
                }
                else
                {
                    tiles[col * rows + row] =
                            new Tile(row, Tile.getSize() + col * Tile.getSize() + (int)(Tile.getSize()/1.5) * col,
                                    2*Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row,
                                    col, row,
                                    Color.white,
                                    true,
                                    true);
                }

            }
        }

    }
    public void drawMap(Graphics g, Camera camera) {
        for (Tile tile : tiles) {
            tile.setX(tile.getX() + camera.cameraX);
            tile.setY(tile.getY() + camera.cameraY);
            tile.draw(g);
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

}
