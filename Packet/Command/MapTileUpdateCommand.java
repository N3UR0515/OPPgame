package Packet.Command;

import Map.Map;
import Packet.Packet;
import Character.*;
import Character.Character;
import Map.Tile.Tile;
import PickUp.PickUpStore;

import java.util.HashMap;

public class MapTileUpdateCommand extends PacketCommand{
    public MapTileUpdateCommand(Packet packet, HashMap<Integer, Character> characters, Map map, Camera camera) {
        super(packet, characters, map, camera);
    }

    @Override
    public void execute() {
        Tile tile = map.getTileByLoc(packet.getX(), packet.getY());
        if(tile.getPickUp() == null)
        {
            tile.setPickUp(PickUpStore.getPickUp("Heal"));
        }
        else
        {
            tile.setPickUp(null);
        }
    }

    @Override
    public void undo() {
    }
}
