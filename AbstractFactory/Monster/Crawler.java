package AbstractFactory.Monster;

import Character.Enemies.Enemy;
import FlyWeight.FlyWeightFactory;
import FlyWeight.MonsterImage;
import Character.Character;
import Character.Player;
import Map.Map;
import Map.Tile.HiderTile;
import Map.Tile.Tile;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Crawler extends Enemy {

    public Crawler(int HP, Map map, int rel_x, int rel_y){
        super(HP, map, rel_x, rel_y);
    }
    @Override
    public void createMonsterImage() {
        try {
            monsterImage = FlyWeightFactory.getMonsterImage("FlyWeight/Images/crawler.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCharacter(Character character) {
        getRealLoc();
        triangle.setCenterX(x);
        triangle.setCenterY(y);

        if(checkDistance((Player) character, map.getTileByLoc(rel_x, rel_y)))
        {
            character.damageCharacter();
        }
        else
        {
            if(map.getTileByLoc(character.getRel_x(), character.getRel_y()).getDistance(parent.getRel_x(), parent.getRel_y()) < 27 * Tile.getSize())
                seekPlayer((Player) character);
        }
    }

    @Override
    public void drawCharacter(Graphics g, int x, int y, int HP) {
        if (map.getTileByLoc(rel_x, rel_y).getClass() != HiderTile.class) {
            this.getRealLoc();
            monsterImage.draw(g,this.x, this.y, this.HP);
        }
    }
}
