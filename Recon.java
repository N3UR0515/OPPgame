import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class Recon implements PlayerStrategy {
    @Override
    public boolean attack(GameContainer container, int x, int y, int rel_x, int rel_y, Map map, Player player) {
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
                player.setAttackTile(tileShortDistance);
                return true;
            }
        }



        return false;
    }
}
