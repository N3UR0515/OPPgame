package Map.Tile;

import org.newdawn.slick.Color;

public class RegularTile extends Tile{
    public RegularTile(boolean id, int x, int y, int tx, int ty, String effect) {
        super(id, x, y, tx, ty, Color.white, effect);
    }
}
