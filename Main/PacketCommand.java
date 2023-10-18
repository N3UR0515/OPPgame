package Main;

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
        return characters;
    }

    abstract void execute();
}