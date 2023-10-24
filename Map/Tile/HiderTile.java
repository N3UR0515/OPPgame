package Map.Tile;

import org.newdawn.slick.Color;

public class HiderTile extends Tile{
    public HiderTile(boolean id, int x, int y, int tx, int ty, String effect) {
        super(id, x, y, tx, ty, Color.lightGray, effect);
    }

    @Override
    public Tile copy() {
        return new HiderTile(this.id, this.getX(), this.getY(), this.getTrel_x(), this.getTrel_y(), "");
    }
}
