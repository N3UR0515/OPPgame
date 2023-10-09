import java.util.HashMap;

public interface PacketCommand {
    void execute(Packet packet, HashMap<Integer, Character> characters, Map map, Camera camera);
}