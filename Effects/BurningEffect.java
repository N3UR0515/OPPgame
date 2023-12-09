package Effects;
import Character.Character;
import Map.Tile.FieryTile;
import Map.Tile.Tile;

public class BurningEffect extends Effect{

    int burningRemains = 0;
    @Override
    public void affect(Character character) {
        Tile tile = character.getMap().getTileByLoc(character.getRel_x(), character.getRel_y());
        if(tile instanceof FieryTile)
        {
            burningRemains = 3;
        } else if (burningRemains > 0) {
            burningRemains--;
            character.damageCharacter();
        }

        affectNext(character);
    }
}
