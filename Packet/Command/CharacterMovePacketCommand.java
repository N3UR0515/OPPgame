package Packet.Command;

import AbstractFactory.Monster.Walker;
import AbstractFactory.MonsterFactory;
import AbstractFactory.MutantFactory;
import Effects.BleedingEffect;
import Effects.GetHitEffect;
import Effects.IgnitingEffect;
import Character.Enemies.Enemy;
import Map.Map;
import Map.Tile.FieryTile;
import Map.Tile.HiderTile;
import Map.Tile.Tile;
import Packet.Packet;
import Server.Server;
import org.junit.runner.manipulation.Ordering;
import org.newdawn.slick.Color;
import Character.*;
import Character.Character;

import java.util.HashMap;

public class CharacterMovePacketCommand extends PacketCommand {

    public CharacterMovePacketCommand(Packet packet, HashMap<Integer, Character> characters, Map map, Camera camera) {
        super(packet, characters, map, camera);
    }

    @Override
    public void execute() {
        if(characters.containsKey(packet.getId()))
        {
            Character temp = characters.get(packet.getId());
            map.getTileByLoc(temp.getRel_x(), temp.getRel_y()).setOnTile(null);
            temp.setRel_y(packet.getY());
            temp.setRel_x(packet.getX());
            map.getTileByLoc(temp.getRel_x(), temp.getRel_y()).setOnTile(temp);
            characters.replace(packet.getId(), temp);

            if(packet.getHP() <= 0)
            {
                map.getTileByLoc(packet.getX(), packet.getY()).setTexture(Color.red);
                map.getTileByLoc(packet.getX(), packet.getY()).setOnTile(null);
            }

        }
        else
        {
            //Character character = new Walker(8, map, packet.getX(), packet.getY());
            //character.setEffects(new IgnitingEffect(), new BleedingEffect(), new GetHitEffect());
            //characters.put(packet.getId(), character);
            Enemy enemy = new MonsterFactory().GetEnemy(packet.getEnemyType(), packet.getX(), packet.getY(), map);
            //Enemy enemy = new Walker(8, map, packet.getX(), packet.getY());
            enemy.setEffects(new IgnitingEffect(), new BleedingEffect(), new GetHitEffect());
            characters.put(packet.getId(), enemy);
            enemy.createMonsterImage();
            map.getTileByLoc(packet.getX(), packet.getY()).setOnTile(characters.get(packet.getId()));
        }
    }

    @Override
    public void undo() {
        if(packet.getHP() <= 0)
        {
            Tile tile = map.getTileByLoc(packet.getX(), packet.getY());
            if( tile instanceof FieryTile)
                tile.setTexture(Color.yellow);
            else if(tile instanceof HiderTile)
                tile.setTexture(Color.gray);
            else
                tile.setTexture(Color.white);

        }
    }
}
