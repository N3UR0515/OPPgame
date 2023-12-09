package Packet.Command;

import Effects.BleedingEffect;
import Effects.GetHitEffect;
import Effects.IgnitingEffect;
import Map.Map;
import Packet.Packet;
import Character.*;
import Character.Character;

import java.util.HashMap;

public class DamagePlayerPacketCommand extends PacketCommand {
    public DamagePlayerPacketCommand(Packet packet, HashMap<Integer, Character> characters, Map map, Camera camera) {
        super(packet, characters, map, camera);
    }

    @Override
     public void execute() {
        if(characters.containsKey(packet.getId()))
        {
            Character temp = characters.get(packet.getId());
            temp.setHP(packet.getHP());
            characters.replace(packet.getId(), temp);
        }
        else
        {
            Player player = new Player(10, map, packet.getX(), packet.getY());
            player.setEffects(new BleedingEffect(), new GetHitEffect());
            characters.put(packet.getId(), player);
        }
    }

    @Override
    public void undo() {

    }
}
