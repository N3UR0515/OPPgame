package AbstractFactory.Monster;

import AbstractFactory.EnemyFactory;
import AbstractFactory.MonsterFactory;
import Character.Enemies.Enemy;
import FlyWeight.FlyWeightFactory;
import FlyWeight.MonsterImage;
import Map.Map;
import Map.Tile.HiderTile;
import Packet.Builder.ChangeOfEnemyPositionPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.PacketDirector;
import Server.Server;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import Character.Character;
import Character.Player;
import Packet.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.newdawn.slick.SlickException;

public class Witch extends Enemy {
    public Witch(int HP, Map map, int rel_x, int rel_y) {
        super(HP, map, rel_x, rel_y);
    }
    @Override
    public void createMonsterImage() {
        try {
            monsterImage = FlyWeightFactory.getMonsterImage("FlyWeight/Images/witch.png");
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

        }
    }

    @Override
    public void damageCharacter() {
        super.damageCharacter();
    }


    @Override
    public void drawCharacter(Graphics g, int x, int y, int HP) {
        if (map.getTileByLoc(rel_x, rel_y).getClass() != HiderTile.class) {
            this.getRealLoc();
            monsterImage.draw(g,this.x, this.y, this.HP);
        }
    }
}
