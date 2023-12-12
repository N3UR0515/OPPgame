package Map;

import Character.Enemies.Enemy;
import Iterator.AreaIterator;
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
    protected final List<CharacterHandler> characters = new ArrayList<>();
    private AreaIterator iterator = new AreaIterator();

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
        iterator.setDefaultX(x);
        iterator.setDefaultY(y);

        CharacterHandler handler = (CharacterHandler) iterator.find((ArrayList<CharacterHandler>)characters);
        if (handler == null) {
            return;
        }
        if (!damagedOnes.contains(handler)) {
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
