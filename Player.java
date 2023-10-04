import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Player extends Character implements Serializable {
    private Tile attackTile = null;

    public Player(int HP, Map map, int rel_x, int rel_y)
    {
        super(HP, map, rel_x, rel_y);
    }

    public void endAttack()
    {
        attackTile = null;
    }
    public Tile getAttackTile()
    {
        return attackTile;
    }

    private boolean attack(GameContainer container)
    {
        Input input = container.getInput();

        if(input.isMousePressed(0))
        {
            int mouseX = input.getMouseX();
            int mouseY = input.getMouseY();

            if(mouseX - x < 30 && mouseY - y < 30)
            {
                int coordX = (mouseX - x) / 16;
                int coordY = (mouseY- y + 5) / 20;

                int [][] directionsEven = {
                        {0, 0}, {-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}
                };
                int[][] directionsOdd = {
                        {0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
                };

                Tile tileShortDistance = null;
                int distance = map.getRows() * Tile.getSize() * map.getCols() * Tile.getSize();

                int[][] directions ={};

                if(map.getTileByLoc(rel_x, rel_y).id)
                {
                    directions = directionsEven;
                }
                else
                {
                    directions = directionsOdd;
                }

                for(int[] dir : directions)
                {
                    int dx = dir[0];
                    int dy = dir[1];

                    if (rel_x + dx >= 0 && rel_x + dx < map.getCols() && rel_y + dy >= 0 && rel_y + dy < map.getRows())
                    {
                        Tile tile = map.getTileByLoc(rel_x + dx, rel_y + dy);
                        if(tile.isAvailable())
                        {
                            if(tileShortDistance == null)
                            {
                                tileShortDistance = tile;
                                distance = tile.getDistance(mouseX, mouseY);
                            } else if (tile.getDistance(mouseX, mouseY) < distance) {
                                tileShortDistance = tile;
                                distance = tile.getDistance(mouseX, mouseY);
                            }
                        }
                    }
                }

                assert tileShortDistance != null;
                System.out.println(tileShortDistance.getTrel_x());
                System.out.println(tileShortDistance.getTrel_y());
                attackTile = tileShortDistance;
                return true;
            }
        }



        return false;
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

        int tempRel_x = 0;
        int tempRel_y = 0;

        // Move the player based on the flags
        if (isMovingRight) {
            tempRel_x++;
        } else if (isMovingLeft) {
            tempRel_x--;
        }
        if (isMovingUp) {
            tempRel_y--;
        } else if (isMovingDown) {
            tempRel_y++;
        }

        //Check if moving to an unavailable tile. If so - do not move
        if (!map.getTileByLoc(rel_x + tempRel_x, rel_y + tempRel_y).isAvailable()) {
            return false;
        }
        //Moving to an available tile
        else {
            rel_x += tempRel_x;
            rel_y += tempRel_y;
        }

        getRealLoc();
        triangle.setCenterX(x);
        triangle.setCenterY(y);

        return isMovingDown ||
                isMovingLeft ||
                isMovingUp ||
                isMovingRight ||
                attack(container);
    }

    @Override
    public void updateCharacter(Character character) {

    }

    @Override
    public void drawCharacter(Graphics g)
    {
        getRealLoc();
        triangle.setCenterX(x);
        triangle.setCenterY(y);
        g.setColor(org.newdawn.slick.Color.red);
        g.fill(triangle);
    }

    public void drawHealth(Graphics g) {
        int outerBoxX = 50;
        int outerBoxY = 50;
        int outerBoxWidth = 100;
        int outerBoxHeight = 20;

        int innerBoxWidth = (int) ((double) HP / 10 * outerBoxWidth);

        g.setColor(org.newdawn.slick.Color.red);
        g.fillRect(outerBoxX, outerBoxY, outerBoxWidth, outerBoxHeight);

        g.setColor(org.newdawn.slick.Color.green);
        g.fillRect(outerBoxX, outerBoxY, innerBoxWidth, outerBoxHeight);
    }

}
