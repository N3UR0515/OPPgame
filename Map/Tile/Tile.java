package Map.Tile;

import org.lwjgl.Sys;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Polygon;
import Character.Character;
import PickUp.*;
import java.io.Serializable;
import java.util.Objects;

public abstract class Tile implements Serializable {
    private static int size = 10;
    public boolean id;
    private int x;             // X-coordinate of the tile
    private int y;             // Y-coordinate of the tile
    private int trel_x;
    private int trel_y;
    private Color texture;    // Texture or color of the tile
    private Character onTile = null;

    public PickUp getPickUp() {
        return pickUp;
    }

    public void setPickUp(PickUp pickUp) {
        System.out.println("hello");
        this.pickUp = pickUp;
    }

    private PickUp pickUp = null;
    private Color borderTexture;


    // Constructor to initialize a Map.Tile.Map.Tile object
    public Tile(boolean id, int x, int y, int tx, int ty, Color texture, String pickUp) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.trel_x = tx;
        this.trel_y = ty;
        this.texture = texture;
        if (!Objects.equals(pickUp, "")){
            this.pickUp = PickUpStore.getPickUp(pickUp);
        }
    }

    // Getters and setters for the Map.Tile.Map.Tile attributes
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
        /*if (Objects.equals(pickUp.getPickupCode(), "Key")) {
            borderTexture = Color.yellow;
        } else if(Objects.equals(pickUp.getPickupCode(), "Heal")) {
            borderTexture = Color.red;
        }*/
        g.setColor(texture);
        g.fill(createHexagon(x, y, size));
        if(pickUp != null && pickUp.getPickupCode().equals("Heal"))
        {
            borderTexture = Color.red;
            g.setColor(borderTexture);
            g.draw(createHexagon(x, y, size));
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

    public Character getOnTile() {
        return onTile;
    }

    public void setOnTile(Character onTile) {
        this.onTile = onTile;
    }

    public abstract Tile copy();
}
