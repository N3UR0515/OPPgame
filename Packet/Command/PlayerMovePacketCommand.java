package Packet.Command;

import Map.Map;
import Packet.Packet;
import Character.*;
import Character.Character;

import java.util.HashMap;

public class PlayerMovePacketCommand extends PacketCommand {

    public PlayerMovePacketCommand(Packet packet, HashMap<Integer, Character> characters, Map map, Camera camera) {
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
        }
        else
        {
            characters.put(packet.getId(), new Player(10, map, packet.getX(), packet.getY()));
            //map.getTileByLoc(packet.getX(), packet.getY()).setOnTile(characters.get(packet.getId()));
        }
    }
}
