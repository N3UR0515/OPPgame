package Main;

import Tile.Tile;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import java.io.Serializable;

public abstract class Character implements Serializable {
    protected int x;
    protected  int y;
    protected int rel_x;
    protected int rel_y;
    protected int HP;
    protected final Polygon triangle;
    protected Map map;
    protected Camera camera;
    public int id = 0;

    public Character(int HP, Map map, int rel_x, int rel_y)
    {
        this.HP = HP;
        this.map = map;
        this.rel_x = rel_x;
        this.rel_y = rel_y;

        Tile tile = map.getTileByLoc(rel_x, rel_y);
        x = tile.getX();
        y = tile.getY();

        triangle = new Polygon();
        triangle.addPoint(x, y - 5);
        triangle.addPoint(x -5, y + 5);
        triangle.addPoint(x + 5, y +5);
    }

    public Character(int HP, Map map, int rel_x, int rel_y, Camera camera)
    {
        this.HP = HP;
        this.map = map;
        this.rel_x = rel_x;
        this.rel_y = rel_y;
        this.camera = camera;

        Tile tile = map.getTileByLoc(rel_x, rel_y);
        x = tile.getX();
        y = tile.getY();

        triangle = new Polygon();
        triangle.addPoint(x + camera.cameraX, y - 5 + camera.cameraY); // Top vertex
        triangle.addPoint(x - 5 + camera.cameraX, y + 5 + camera.cameraY); // Bottom-left vertex
        triangle.addPoint(x + 5 + camera.cameraX, y + 5 + camera.cameraY); // Bottom-right vertex
    }

    public abstract boolean updateCharacter(GameContainer container);
    public abstract void updateCharacter(Character character);

    public int getX()
    {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getRel_x() {
        return rel_x;
    }

    public int getRel_y() {
        return rel_y;
    }

    public void setRel_x(int x) {
        rel_x = x;
        getRealLoc();
        triangle.setCenterX(this.x);
    }

    public void setRel_y(int y) {
        rel_y = y;
        getRealLoc();
        triangle.setCenterY(this.y);
    }

    public void damageCharacter() {
        HP--;
    }

    protected void getRealLoc() {
        Tile tile = map.getTileByLoc(rel_x, rel_y);
        x = tile.getX();
        y = tile.getY();
    }
    public int getHP()
    {
        return HP;
    }

    public void setHP(int health)
    {
        this.HP = health;
    }

    public abstract void drawCharacter(Graphics g);
}
