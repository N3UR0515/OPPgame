package Character;

import Map.Tile.*;
import Map.Map;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.io.Serializable;

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

        if(input.isMousePressed(0)) // Checking for left mouse button
        {
            int mouseX = input.getMouseX();
            int mouseY = input.getMouseY(); // X and Y of the mouse

            int range = Tile.getSize() * 3;  // Map.Tile.getSize() gets half the size of a tile
            // so the range includes the tile that the player is standing on and one tile to every side

            if(mouseX - x < range && mouseY - y < range) // this is taking the relative distance from where player
                // is drawn
            {
                /*int coordX = (mouseX - x) / 16; // calculates the tile difference in x axis of the player position
                // and mouse position
                // these were used in the earlier implementation
                int coordY = (mouseY- y + 5) / 20;*/

                int [][] directionsEven = { // directions of how to get to all the surrounding tiles
                        {0, 0}, {-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}
                };
                int[][] directionsOdd = {
                        {0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
                };

                Tile tileShortDistance = null; // will be searching for shortest distance between mouse and tiles
                int distance = map.getRows() * Tile.getSize() * map.getCols() * Tile.getSize(); // max possible value

                int[][] directions ={};

                if(map.getTileByLoc(rel_x, rel_y).id) // depending if player is standing on odd or even tile
                    // the ways to get to surrounding tiles changes
                    // id shows if the tile is even or odd
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
                    // checking if the tile is not outside the map
                    {
                        Tile tile = map.getTileByLoc(rel_x + dx, rel_y + dy);
                        if(tile.getClass() != UnavailableTile.class)
                        {
                            if(tileShortDistance == null)
                            {
                                tileShortDistance = tile;
                                distance = tile.getDistance(mouseX, mouseY); // calculation of distance between where the
                                // drawn center of the tile is and where the mouse was clicked on screen
                                // the real distance is used and not the relative one
                            } else if (tile.getDistance(mouseX, mouseY) < distance) {
                                tileShortDistance = tile;
                                distance = tile.getDistance(mouseX, mouseY); // the tile that is drawn closest to the mouse wins
                            }
                        }
                    }
                }

                assert tileShortDistance != null;
                System.out.println(tileShortDistance.getTrel_x());
                System.out.println(tileShortDistance.getTrel_y());
                attackTile = tileShortDistance;
                return true; // attack happened
            }
        }



        return false; // attack didn't happen
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

        boolean Attack = attack(container);

        //Check if moving to an unavailable tile. If so - do not move
        Tile tempTile = map.getTileByLoc(rel_x + tempRel_x, rel_y + tempRel_y);
        if (tempTile.getClass() == UnavailableTile.class ||
                (tempTile.getOnTile() != null && !Attack)) {
            return false;
        }
        //Moving to an available tile
        else {
            rel_x += tempRel_x;
            rel_y += tempRel_y;
        }

        /*getRealLoc();
        triangle.setCenterX(x);
        triangle.setCenterY(y);*/

        return isMovingDown ||
                isMovingLeft ||
                isMovingUp ||
                isMovingRight ||
                Attack;
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
        // Checks if player is NOT on a hiding tile. If true - draws player
        if (map.getTileByLoc(rel_x, rel_y).getClass() != HiderTile.class) {
            g.setColor(org.newdawn.slick.Color.red);
            g.fill(triangle);
            g.drawOval(x, y, 5, 5);
        }
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
