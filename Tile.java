import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Polygon;

public class Tile {
    private static int size = 10;
    public boolean id;
    private int x;             // X-coordinate of the tile
    private int y;             // Y-coordinate of the tile
    private int trel_x;
    private int trel_y;
    private Color texture;    // Texture or color of the tile
    private boolean isVisible; // Visibility status of the tile
    private boolean isAvailable; // Availability status of the tile

    // Constructor to initialize a Tile object
    public Tile(boolean id, int x, int y, int tx, int ty, Color texture, boolean isVisible, boolean isAvailable) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.trel_x = tx;
        this.trel_y = ty;
        this.texture = texture;
        this.isVisible = isVisible;
        this.isAvailable = isAvailable;
    }

    // Getters and setters for the Tile attributes
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getTexture() {
        return texture;
    }

    public void setTexture(Color texture) {
        this.texture = texture;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getTrel_x()
    {
        return trel_x;
    }

    public int getTrel_y()
    {
        return trel_y;
    }

    public static int getSize()
    {
        return size;
    }

    public void draw(Graphics g) {
        if (isVisible) {
            g.setColor(texture);
            g.fill(createHexagon(x, y, size));
        }
    }

    private Polygon createHexagon(float centerX, float centerY, int size) {
        Polygon hexagon = new Polygon();

        for (int i = 0; i < 6; i++) {
            float angle = (float) (Math.PI / 3.0 * i);
            float xOffset = size * (float) Math.cos(angle);
            float yOffset = size * (float) Math.sin(angle);
            hexagon.addPoint(centerX + xOffset, centerY + yOffset);
        }

        return hexagon;
    }
}
