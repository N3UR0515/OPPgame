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
import org.lwjgl.Sys;
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

    public synchronized void removeCharacter(CharacterHandler character) {
        boolean hasRemoved = characters.remove(character);
        if (hasRemoved) {
            character.removeArea(this);
        }
    }

    public void sendAttack(int x, int y, List<CharacterHandler> damagedOnes) throws IOException {
        for (CharacterHandler handler : characters) {
            if(damagedOnes.contains(handler))
                break;
            //System.out.println(handler.characterModel.id + "ID");
            if (handler.characterModel.getRel_x() == x && handler.characterModel.getRel_y() == y){
                //System.out.println("Hello");
                handler.characterModel.damageCharacter();
                damagedOnes.add(handler);
                System.out.println(handler.characterModel.id + " HP:" + handler.characterModel.getHP());
                if (handler instanceof ClientHandler) {
                    PacketBuilder builder = new DamagePlayerPacketBuilder();
                    PacketDirector.constructDamagePlayerPacket(builder, (Player) handler.characterModel);
                    Packet pa = builder.getPacket();
                    handler.sendPacket(pa);
                }
                if(handler.characterModel.getHP() <= 0){
                    Turnline turnline = Turnline.getInstance();

                    Server.map.getTileByLoc(handler.characterModel.getRel_x(), handler.characterModel.getRel_y()).setTexture(Color.red);
                    Server.map.getTileByLoc(handler.characterModel.getRel_x(), handler.characterModel.getRel_y()).setOnTile(null);
                    PacketBuilder builder = new ChangeOfEnemyPositionPacketBuilder();
                    PacketDirector.constructChangeOfEnemyPositionPacket(builder, handler.characterModel);
                    Server.broadcastPacket(builder.getPacket());
                    synchronized (turnline) {
                        turnline.Remove(handler.characterModel);
                    }
                }
            }
        }
    }
}
