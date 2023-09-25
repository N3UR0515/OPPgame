import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;

import java.io.Serializable;

public class Player extends Character implements Serializable {

    public Player(int HP, Map map, int rel_x, int rel_y)
    {
        super(HP, map, rel_x, rel_y);
    }

    @Override
    public boolean updateCharacter(GameContainer container) {
        Input input = container.getInput();

        // Reset movement flags
        boolean isMovingRight = false;
        boolean isMovingLeft = false;
        boolean isMovingUp = false;
        boolean isMovingDown = false;
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
                isMovingLeft ||
                isMovingUp ||
                isMovingRight;
    }

    @Override
    public void updateCharacter(Character character) {

    }

    @Override
    public void drawCharacter(Graphics g)
    {
        g.setColor(org.newdawn.slick.Color.red);
        g.fill(triangle);
    }
}
