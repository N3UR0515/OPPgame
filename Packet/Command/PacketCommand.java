package Packet.Command;

import Map.Map;
import Packet.Packet;
import Character.*;
import Character.Character;

import java.util.HashMap;

public abstract class PacketCommand {
    Packet packet;
    HashMap<Integer, Character> characters;
    Map map;
    Camera camera;

    PacketCommand(Packet packet, HashMap<Integer, Character> characters, Map map, Camera camera)
    {
        this.packet = packet;
        this.characters = characters;
        this.map = map;
        this.camera = camera;
    }

    public HashMap<Integer, Character> getResults()
    {
        if(characters != null)
            return characters;
        return new HashMap<Integer, Character>();
    }

    public abstract void execute();
}