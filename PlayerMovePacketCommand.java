import java.util.HashMap;

public class PlayerMovePacketCommand implements PacketCommand{

    @Override
    public void execute(Packet packet, HashMap<Integer, Character> characters, Map map, Camera camera) {
        if(characters.containsKey(packet.getId()))
        {
            Character temp = characters.get(packet.getId());
            temp.setRel_y(packet.getY());
            temp.setRel_x(packet.getX());
            characters.replace(packet.getId(), temp);
        }
        else
        {
            characters.put(packet.getId(), new Player(10, map, packet.getX(), packet.getY()));
        }
    }
}
