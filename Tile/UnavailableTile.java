package Tile;

import org.newdawn.slick.Color;

public class UnavailableTile extends Tile{
    public UnavailableTile(boolean id, int x, int y, int tx, int ty, String pickUp) {
        super(id, x, y, tx, ty, Color.black, pickUp);
    }
}
