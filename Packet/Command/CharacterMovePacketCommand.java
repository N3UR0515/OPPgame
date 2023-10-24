package Packet.Command;

import Character.Enemies.Enemy;
import Character.Enemies.ZombieOld;
import Map.Map;
import Packet.Packet;
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
            characters.put(packet.getId(), new ZombieOld(10, map, packet.getX(), packet.getY(), camera));
            map.getTileByLoc(packet.getX(), packet.getY()).setOnTile(characters.get(packet.getId()));
        }
    }
}
