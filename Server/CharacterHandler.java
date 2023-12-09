package Server;

import Map.Area;
import Map.Tile.FieryTile;
import Packet.Builder.DamagePlayerPacketBuilder;
import Packet.Builder.HealthPickUpSetPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.PacketDirector;
import Packet.Packet;
import Character.Character;
import Character.Player;
import Map.Tile.Tile;

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
    protected void addAreas()
    {
        List<Area> newAreas = Server.getAreas(characterModel.getRel_y(), characterModel.getRel_x());
        List<Area> oldOnes = new ArrayList<>(this.areas);
        for (int i = 0; i < this.areas.size(); i++){
            this.areas.get(i).removeCharacter(this);
        }
        this.areas.removeAll(oldOnes);
        this.areas.addAll(newAreas);
        for(Area area : this.areas)
        {
            area.addCharacter(this);
        }
    }
    protected boolean isOnFireTile()
    {
        return Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y()).getClass() == FieryTile.class;
    }
    protected void damagePacket() throws IOException {
        PacketBuilder dmgBuilder = new DamagePlayerPacketBuilder();
        PacketDirector.constructDamagePlayerPacket(dmgBuilder, (Player) characterModel);
        Packet toSend = dmgBuilder.getPacket();
        sendPacket(toSend);
    }
    protected void setHealthTile()
    {
        Tile tile = Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y());
        if(tile.getPickUp() != null)
        {
            PacketBuilder builder = new HealthPickUpSetPacketBuilder();
            PacketDirector.constructSetHealthPickupPacket(builder, tile);
            try
            {
                Server.broadcastPacket(builder.getPacket());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y()).setPickUp(null);
        }

    }
    protected boolean checkForPickUp()
    {
        Tile tile = Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y());
        return tile.getPickUp() != null;
    }

    protected void TurnlineNext()
    {
        Turnline turnline = Turnline.getInstance();
        turnline.Next();
    }

    protected void TurnlineRemove()
    {
        Turnline turnline = Turnline.getInstance();
        if(characterModel.getHP() <= 0)
            turnline.Remove(characterModel);
    }

    protected abstract void move();
    protected abstract void attack();
    protected abstract void assignArtifact();
    protected abstract boolean checkForTurn();
    protected abstract void receiveTileDamage();
    protected abstract void usePickUp();
    protected void rollArtifact()
    {
        characterModel.rollArtifactEffect();
    }
    protected abstract boolean checkForAttack();
    protected abstract boolean checkForMove();
    protected void affectSelf()
    {
        characterModel.affectSelf();
    };
    @Override
    public void run() {
        if(checkForTurn())
        {
            if(checkForMove())
            {
                move();
                addAreas();
            }
            if (checkForAttack())
            {
                attack();
            }

            if(isOnFireTile())
            {
                receiveTileDamage();
            }
            affectSelf();
            rollArtifact();

            if(checkForPickUp())
            {
                usePickUp();
                setHealthTile();
            }
            TurnlineNext();
            TurnlineRemove();
        }
    }

}
