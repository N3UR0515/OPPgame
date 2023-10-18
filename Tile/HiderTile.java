package Tile;

import org.newdawn.slick.Color;

public class HiderTile extends Tile{
    public HiderTile(boolean id, int x, int y, int tx, int ty, String pickUp) {
        super(id, x, y, tx, ty, Color.lightGray, pickUp);
    }
}
