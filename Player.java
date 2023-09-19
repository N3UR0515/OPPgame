import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.Graphics;

import java.io.ObjectOutputStream;

public class Player {
    private int x;
    private int y;
    private int rel_x;
    private int rel_y;
    private int HP;
    private final Polygon triangle;
    private Map map;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isMovingUp;
    private boolean isMovingDown;

    public Player(int HP, Map map, int rel_x, int rel_y)
    {
        //x = map.getTile(0).getX();
        //y = map.getTile(0).getY();
        this.rel_x = rel_x;
        this.rel_y = rel_y;
        this.HP = HP;
        this.map = map;
        Tile tile = map.getTileByLoc(rel_x, rel_y);
        x = tile.getX();
        y = tile.getY();

        triangle = new Polygon();
        triangle.addPoint(x, y - 50); // Top vertex
        triangle.addPoint(x - 50, y + 50); // Bottom-left vertex
        triangle.addPoint(x + 50, y + 50); // Bottom-right vertex
    }

    public boolean updatePlayer(GameContainer container) {
        Input input = container.getInput();

        // Reset movement flags
        isMovingRight = false;
        isMovingLeft = false;
        isMovingUp = false;
        isMovingDown = false;
        if (input.isKeyPressed(Input.KEY_Q)) {

            if (rel_x % 2 != 0 && rel_x > 0) {
                isMovingLeft = true;
            }

            if (rel_x % 2 == 0 && rel_y > 0 && rel_x > 0) {
                isMovingUp = true;
                isMovingLeft = true;
            }
        }
        if (input.isKeyPressed(Input.KEY_W) && rel_y > 0) {
            isMovingUp = true;
        }
        if (input.isKeyPressed(Input.KEY_E)) {
            // Cases:
            // When going from an odd x to even, it just adds +1 to rel_x
            if (rel_x % 2 != 0 && rel_x < map.getCols() - 1) {
                isMovingRight = true;
            }
            // When going from an even x to a lower col y
            if (rel_x % 2 == 0 && rel_y > 0 && rel_x < map.getCols() - 1) {
                isMovingUp = true;
                isMovingRight = true;
            }
        }
        if (input.isKeyPressed(Input.KEY_A)) {
            if (rel_x % 2 == 0 && rel_x > 0) {
                isMovingLeft = true;
            }

            if (rel_x % 2 != 0 && rel_y < map.getRows() - 1 && rel_x > 0) {
                isMovingDown = true;
                isMovingLeft = true;
            }
        }
        if (input.isKeyPressed(Input.KEY_S) && rel_y < map.getRows() - 1) {
            isMovingDown = true;
        }

        if (input.isKeyPressed(Input.KEY_D)) {
            if (rel_x % 2 == 0 && rel_x < map.getCols() - 1) {
                isMovingRight = true;
            }

            if (rel_x % 2 != 0 && rel_y < map.getRows() - 1 && rel_x < map.getCols() - 1) {
                isMovingDown = true;
                isMovingRight = true;
            }
        }



        // Move the player based on the flags
        if (isMovingRight) {
            rel_x++;
        } else if (isMovingLeft) {
            rel_x--;
        }
        if (isMovingUp) {
            rel_y--;
        } else if (isMovingDown) {
            rel_y++;
        }

        getRealLoc();
        triangle.setCenterX(x);
        triangle.setCenterY(y);

        return isMovingDown ||
                isMovingLeft||
                isMovingUp  ||
                isMovingRight;
    }


    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }

    public int getRel_x()
    {
        return rel_x;
    }
    public int getRel_y()
    {
        return rel_y;
    }
    public void damagePlayer()
    {
        HP--;
    }
    private void getRealLoc()
    {
        Tile tile = map.getTileByLoc(rel_x, rel_y);
        x = tile.getX();
        y = tile.getY();
    }

    public void drawPlayer(Graphics g)
    {
        g.setColor(org.newdawn.slick.Color.red);
        g.fill(triangle);
    }
}
