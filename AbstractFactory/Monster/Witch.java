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
        private final ArrayList<Enemy> children;
    public Witch(int HP, Map map, int rel_x, int rel_y) {
        super(HP, map, rel_x, rel_y);
        children = new ArrayList<>();
        EnemyFactory factory = new MonsterFactory();
        Enemy child = factory.GetEnemy("WALKING", rel_x, rel_y);
        child.id = id + id*100;
        child.setParent(this);
        children.add(child);
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
            if(children.isEmpty())
            {
                EnemyFactory factory = new MonsterFactory();
                Enemy child = factory.GetEnemy("CRAWLING", rel_x, rel_y);
                child.id = id + id*100;
                child.setParent(this);
                children.add(child);
            }
            for(Enemy child : children)
            {
                child.updateCharacter(character);
            }
        }
    }

    public void add(Enemy child)
    {
        children.add(child);
    }

    public synchronized void remove(Enemy child)
    {
        children.remove(child);
    }
    public ArrayList<Enemy> getChildren()
    {
        return children;
    }

    @Override
    public void damageCharacter() {
        super.damageCharacter();

        synchronized (children) {
            Iterator<Enemy> iterator = children.iterator();
            while (iterator.hasNext()) {
                Enemy child = iterator.next();
                child.damageCharacter();
                if (child.getHP() <= 0) {
                    PacketBuilder builder;
                    builder = new ChangeOfEnemyPositionPacketBuilder();
                    PacketDirector.constructChangeOfEnemyPositionPacket(builder, child);

                    Packet packet = builder.getPacket();
                    try {
                        Server.broadcastPacket(packet);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    iterator.remove(); // Use the iterator to remove the current element
                }
            }
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
