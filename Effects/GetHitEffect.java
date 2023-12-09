package Effects;
import Character.Character;
import Map.Tile.Tile;
import org.newdawn.slick.Color;

public class GetHitEffect extends Effect{
    int lifesBefore = -1;
    @Override
    public void affect(Character character) {
        if(lifesBefore == -1)
        {
            lifesBefore = character.getHP();
        }
        else
        {
            int lifesNow = character.getHP();
            int diff = lifesBefore - lifesNow;
            if(diff > 0)
            {
                Tile tile = character.getMap().getTileByLoc(character.getRel_x(), character.getRel_y());
                if(tile.getTexture().getRed() == 255 && tile.getTexture().getGreen() == tile.getTexture().getBlue())
                {
                    int count = (int)((255-tile.getTexture().getGreen())/25);
                    count += diff;
                    Color color = new Color(255, 255-25*count, 255-25*count);
                    tile.setTexture(color);
                }
            }
            lifesBefore = lifesNow;
        }
        affectNext(character);
    }
}
