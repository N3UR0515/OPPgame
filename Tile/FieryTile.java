package Tile;

import org.newdawn.slick.Color;

public class FieryTile extends Tile{
    public FieryTile(boolean id, int x, int y, int tx, int ty, String pickUp) {
        super(id, x, y, tx, ty, Color.orange, pickUp);
    }
}
