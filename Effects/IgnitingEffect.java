package Effects;
import Character.Character;
import Map.Tile.FieryTile;
import Map.Tile.Tile;

public class IgnitingEffect extends Effect{
    boolean burning = false;
    @Override
    public void affect(Character character) {
        Tile tile = character.getMap().getTileByLoc(character.getRel_x(), character.getRel_y());

        if(tile instanceof FieryTile && !burning)
        {
            burning = true;
        }
        else if (burning)
        {
            Tile newTile = new FieryTile(tile.id, tile.getX(), tile.getY(), tile.getTrel_x(), tile.getTrel_y(), "");
            character.getMap().replaceTile(character.getRel_x(), character.getRel_y(), newTile);
            burning = false;
        }

        affectNext(character);
    }
}
