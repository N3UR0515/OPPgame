
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class MachineGunner implements PlayerStrategy {
    @Override
    public boolean attack(GameContainer container, int x, int y, int rel_x, int rel_y, Map map, Player player)
    {
        Input input = container.getInput();

        if(input.isMousePressed(0)) // Checking for left mouse button
        {
            int mouseX = input.getMouseX();
            int mouseY = input.getMouseY(); // X and Y of the mouse

            int range = Tile.getSize() * 5;  // Tile.getSize() gets half the size of a tile
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
                int[][] directionsNext ={};

                if(map.getTileByLoc(rel_x, rel_y).id) // depending if player is standing on odd or even tile
                // the ways to get to surrounding tiles changes
                // id shows if the tile is even or odd
                {
                    directions = directionsEven;
                    directionsNext = directionsOdd;
                }
                else
                {
                    directions = directionsOdd;
                    directionsNext = directionsEven;
                }

                for(int[] dir : directions)
                {
                    for(int[] dirNext : directionsNext) {
                        int dx = dir[0] + dirNext[0];
                        int dy = dir[1] + dirNext[1];
                        if (rel_x + dx >= 0 && rel_x + dx < map.getCols() && rel_y + dy >= 0 && rel_y + dy < map.getRows())
                        // checking if the tile is not outside the map
                        {
                            Tile tile = map.getTileByLoc(rel_x + dx, rel_y + dy);
                            if (tile.isAvailable()) {
                                if (tileShortDistance == null) {
                                    tileShortDistance = tile;
                                    distance = tile.getDistance(mouseX, mouseY); // calculation of distance between where the
                                    // drawn center of the tile is and where the mouse was clicked on screen
                                    // the real distance is used and not the relative one
                                } else if (tile.getDistance(mouseX, mouseY) < distance) {
                                    tileShortDistance = tile;
                                    distance = tile.getDistance(mouseX, mouseY); // the tile that is drawn closest to the mouse wins
                                }
                            }
                        } else {
                            dx = dir[0];
                            dy = dir[1];
                            if (rel_x + dx >= 0 && rel_x + dx < map.getCols() && rel_y + dy >= 0 && rel_y + dy < map.getRows())
                            // checking if the tile is not outside the map
                            {
                                Tile tile = map.getTileByLoc(rel_x + dx, rel_y + dy);
                                if (tile.isAvailable()) {
                                    if (tileShortDistance == null) {
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
                    }
                }

                assert tileShortDistance != null;
                System.out.println(tileShortDistance.getTrel_x());
                System.out.println(tileShortDistance.getTrel_y());
                player.setAttackTile(tileShortDistance);
                return true; // attack happened
            }
        }



        return false; // attack didn't happen
    }
}
