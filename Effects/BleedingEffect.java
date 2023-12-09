package Effects;
import Character.Character;
import Map.Tile.Tile;
import org.newdawn.slick.Color;

public class BleedingEffect extends Effect {
    int remainingTiles = 0;
    public void reset(Character character)
    {
        Tile tile = character.getMap().getTileByLoc(character.getRel_x(), character.getRel_y());
        if(tile.getTexture().getRed() == 255 && tile.getTexture().getGreen() == tile.getTexture().getBlue())
        {
            int count = (int)((255-tile.getTexture().getGreen())/25);
            if(count > remainingTiles)
                remainingTiles = count;
        }
    }
    @Override
    public void affect(Character character) {
        Tile tile = character.getMap().getTileByLoc(character.getRel_x(), character.getRel_y());
        reset(character);

        if(remainingTiles > 0)
        {
            Color color = new Color(255, 255-25*remainingTiles, 255-25*remainingTiles);
            tile.setTexture(color);
            remainingTiles--;
        }


        affectNext(character);
    }
}
