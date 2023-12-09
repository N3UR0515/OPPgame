package Server;

import Artifact.MagicStaff;
import Artifact.WarmQuartz;
import Effects.BleedingOutEffect;
import Effects.BurningEffect;
import Effects.IgnitingEffect;
import Map.Area;
import Map.Tile.FieryTile;
import Map.Tile.Tile;
import Packet.Builder.ChangeOfPlayerPositionPacketBuilder;
import Packet.Builder.DamagePlayerPacketBuilder;
import Packet.Builder.HealthPickUpSetPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.Packet;
import Character.Player;
import Packet.PacketDirector;
import org.lwjgl.Sys;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientHandler extends CharacterHandler {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Packet packet;

    public ClientHandler(Socket clientSocket, int clientId) {
        this.characterId = clientId;
        try {
            characterModel = new Player(10, Server.map, 0, 0);

            assignArtifact();

            characterModel.id = clientId;
            characterModel.setEffects(new BleedingOutEffect());
            Turnline.getInstance().Add(characterModel);

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            out.writeObject(Server.initMap);
            out.writeObject(clientId);

            for(Tile tile : Server.tiles)// the pickups on tiles are on separate generation to ease up on the sending time
            {
                PacketBuilder builder = new HealthPickUpSetPacketBuilder();
                PacketDirector.constructSetHealthPickupPacket(builder, tile);
                sendPacket(builder.getPacket());
            }
            //out.writeObject(characterModel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPacket(Packet packet) throws IOException {
        out.writeObject(packet);
        out.flush();
    }

    @Override
    protected void move() {
        try{
            Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y()).setOnTile(null);
            characterModel.setRel_x(packet.getX());
            characterModel.setRel_y(packet.getY());
            Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y()).setOnTile(characterModel);

            PacketBuilder builder;
            builder = new ChangeOfPlayerPositionPacketBuilder();
            PacketDirector.constructChangeOfPlayerPositonPacket(builder, (Player) characterModel);
            Packet outPacket = builder.getPacket();
            Server.broadcastPacket(outPacket);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void attack() {
        try
        {
            List<Area> areas = Server.getAreas(packet.getY(), packet.getX());
            List<CharacterHandler> damagedOnes = new ArrayList<>();
            for (Area area : areas) {
                area.sendAttack(packet.getX(), packet.getY(), damagedOnes);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void assignArtifact() {
        //Decide which artifact to assign to player.
        //Later on make this players choice
        Random rng = new Random();
        if (rng.nextInt(2) == 0) {
            characterModel.setArtifact(new MagicStaff());
        } else {
            characterModel.setArtifact(new WarmQuartz());
        }
    }

    @Override
    protected boolean checkForTurn() {
        Turnline turnline = Turnline.getInstance();
        return turnline.getCharacter() instanceof Player && turnline.getCharacter().id == characterId;
    }

    @Override
    protected void receiveTileDamage() {
        characterModel.damageCharacter();
        try
        {
            damagePacket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void usePickUp() {
        Tile tile = Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y());
        characterModel.UseAndDeleteEffect(tile.getPickUp().getPickupCode());
        try
        {
            damagePacket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected boolean checkForAttack() {
        if(packet != null)
            return packet.isAttack();
        return false;
    }

    @Override
    protected boolean checkForMove() {
        try
        {
            packet = (Packet) in.readObject();
            if(packet != null)
                return !packet.isAttack();
        }catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    protected void affectSelf() {
        super.affectSelf();
        try{
            damagePacket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
