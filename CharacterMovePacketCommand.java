import org.newdawn.slick.Color;

import java.util.HashMap;

public class CharacterMovePacketCommand extends PacketCommand{

    CharacterMovePacketCommand(Packet packet, HashMap<Integer, Character> characters, Map map, Camera camera) {
        super(packet, characters, map, camera);
    }

    @Override
    void execute() {
        if(characters.containsKey(packet.getId()))
        {
            Character temp = characters.get(packet.getId());
            temp.setRel_y(packet.getY());
            temp.setRel_x(packet.getX());
            characters.replace(packet.getId(), temp);

            if(packet.getHP() <= 0)
                map.getTileByLoc(packet.getX(), packet.getY()).setTexture(Color.red);
        }
        else
        {
            characters.put(packet.getId(), new Enemy(10, map, packet.getX(), packet.getY(), camera) {
            });
        }
    }
}
