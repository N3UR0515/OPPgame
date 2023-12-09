package Effects;
import Character.Character;
import Character.Player;
import Map.Tile.Tile;
import org.newdawn.slick.Color;

public class AttackingEffect extends Effect{
    @Override
    public void affect(Character character) {
        if(character instanceof Player)
        {
            Player player = (Player)(character);
            if(player.getAttackTile() != null && player.getAttackTile().getOnTile() != null)
            {
                Tile tile = player.getAttackTile();
                if(tile.getTexture().getRed() == 255 && tile.getTexture().getGreen() == tile.getTexture().getBlue())
                {
                    int count = (int)((255-tile.getTexture().getGreen())/25);
                    count++;
                    Color color = new Color(255, 255-25*count, 255-25*count);
                    tile.setTexture(color);
                }
            }
        }
        affectNext(character);
    }
}
