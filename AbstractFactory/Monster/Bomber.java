package AbstractFactory.Monster;

import Character.Enemies.Enemy;
import FlyWeight.FlyWeightFactory;
import FlyWeight.MonsterImage;
import FlyWeight.SharedMonsterImage;
import Map.Map;
import Map.Tile.HiderTile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bomber extends Enemy {

    public Bomber(int HP, Map map, int rel_x, int rel_y){
        super(HP, map, rel_x, rel_y);
        monsterImage = createMonsterImage();
    }
    @Override
    protected MonsterImage createMonsterImage() {
        try {
            return FlyWeightFactory.getMonsterImage("FlyWeight/Images/bomber.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
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
