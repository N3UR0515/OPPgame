package Tile;
import PickUp.PickUp;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Polygon;
import PickUp.PickUpStore;
import java.io.Serializable;

public abstract class Tile implements Serializable {
    private static int size = 10;
    public boolean id;
    private int x;             // X-coordinate of the tile
    private int y;             // Y-coordinate of the tile
    private int trel_x;
    private int trel_y;
    private Color texture;    // Texture or color of the tile
    private PickUp pickUp = null;
    private Color borderTexture;

    // Constructor to initialize a Tile.Tile object
    public Tile(boolean id, int x, int y, int tx, int ty, Color texture, String pickUp) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.trel_x = tx;
        this.trel_y = ty;
        this.texture = texture;
        if (pickUp != ""){
            this.pickUp = PickUpStore.getPickUp(pickUp);
            if (pickUp == "Key") {
                borderTexture = Color.yellow;
            } else if(pickUp == "Heal") {
                borderTexture = Color.red;
            }
        } else {
            borderTexture = Color.black;
        }
    }

    // Getters and setters for the Tile.Tile attributes
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

    public int getDistance(int objectX, int objectY)
    {
        return (int) Math.sqrt(Math.pow(objectX - x, 2)+Math.pow(objectY - y,2));
    }

    public void draw(Graphics g) {
        g.setColor(texture);
        g.fill(createHexagon(x, y, size));
        g.setColor(borderTexture);
        g.draw(createHexagon(x, y, size));

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
