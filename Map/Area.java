package Map;

import Character.Enemies.Enemy;
import Packet.Builder.ChangeOfEnemyPositionPacketBuilder;
import Packet.Builder.DamagePlayerPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.Packet;
import Packet.*;
import Character.Character;
import Server.CharacterHandler;
import Server.*;
import Character.*;
import org.newdawn.slick.Color;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Area implements Serializable {
    private final List<CharacterHandler> characters = new ArrayList<>();

    public Area() {}

    public void addCharacter(CharacterHandler character) {
        if (!characters.contains(character)) {
            characters.add(character);
            character.addArea(this);
        }

    }

    public void removeCharacter(CharacterHandler character) {
        boolean hasRemoved = characters.remove(character);
        if (hasRemoved) {
            character.removeArea(this);
        }
    }

    public void sendAttack(int x, int y) throws IOException {
        for (CharacterHandler handler : characters) {
            System.out.println(handler.characterModel.id + "ID");
            if (handler.characterModel.getRel_x() == x && handler.characterModel.getRel_y() == y){
                System.out.println("Hello");
                handler.characterModel.damageCharacter();
                if (handler instanceof ClientHandler) {
                    PacketBuilder builder = new DamagePlayerPacketBuilder();
                    PacketDirector.constructDamagePlayerPacket(builder, (Player) handler.characterModel);
                    Packet pa = builder.getPacket();
                    handler.sendPacket(pa);
                }
                if(handler.characterModel.getHP() <= 0){
                    Turnline turnline = Turnline.getInstance();
                    Server.map.getTileByLoc(handler.characterModel.getRel_x(), handler.characterModel.getRel_y()).setTexture(Color.red);
                    PacketBuilder builder = new ChangeOfEnemyPositionPacketBuilder();
                    PacketDirector.constructChangeOfEnemyPositionPacket(builder, (Enemy) handler.characterModel);
                    Server.broadcastPacket(builder.getPacket());
                    turnline.Remove(handler.characterModel);
                }
            }
        }
    }
}