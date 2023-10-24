package Map.Tile;

import org.newdawn.slick.Color;

public class FieryTile extends Tile{
    public FieryTile(boolean id, int x, int y, int tx, int ty, String effect) {
        super(id, x, y, tx, ty, Color.orange, effect);
    }

    @Override
    public Tile copy() {
        return new FieryTile(this.id, this.getX(), this.getY(), this.getTrel_x(), this.getTrel_y(), "");
    }
}
