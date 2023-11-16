package Server;

import Map.Area;
import Packet.Packet;
import Character.Character;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class CharacterHandler implements Runnable, Serializable {

    protected List<Area> areas = new ArrayList<>(4);
    public int characterId;
    public Character characterModel;
    public void addArea(Area area) {
        if (!this.areas.contains(area)) {
            this.areas.add(area);
        }
    }

    public void removeArea(Area area) {
        this.areas.remove(area);
    }

    public abstract void sendPacket(Packet packet) throws IOException;
}
