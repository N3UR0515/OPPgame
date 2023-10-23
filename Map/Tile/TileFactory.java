package Map.Tile;

import java.io.Serializable;

public class TileFactory implements Serializable {
    public TileFactory() {}
    public Tile createTile(int key, boolean isId, int row, int col) {
        if (key == 0) {
            return new RegularTile(
                    isId,
                    Tile.getSize() + col * Tile.getSize() + (int)(Tile.getSize()/1.5) * col,
                    isId? Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row : 2 * Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row,
                    col,
                    row
            );
        } else if (key == 1) {
            return new HiderTile(
                    isId,
                    Tile.getSize() + col * Tile.getSize() + (int)(Tile.getSize()/1.5) * col,
                    isId? Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row : 2 * Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row,
                    col,
                    row
            );
        } else if (key == 2) {
            return new FieryTile(
                    isId,
                    Tile.getSize() + col * Tile.getSize() + (int)(Tile.getSize()/1.5) * col,
                    isId? Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row : 2 * Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row,
                    col,
                    row
            );
        } else {
            return new UnavailableTile(
                    isId,
                    Tile.getSize() + col * Tile.getSize() + (int)(Tile.getSize()/1.5) * col,
                    isId? Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row : 2 * Tile.getSize() + row * Tile.getSize() + Tile.getSize() * row,
                    col,
                    row
            );
        }
    }

}
