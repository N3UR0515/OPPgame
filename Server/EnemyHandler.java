package Server;

import Artifact.MagicStaff;
import Artifact.WarmQuartz;
import Character.Enemies.Enemy;
import Character.Enemies.EnemyFactory;
import Map.Area;
import Packet.Builder.ChangeOfEnemyPositionPacketBuilder;
import Packet.Builder.DamagePlayerPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.Packet;
import Map.Tile.FieryTile;
import Map.Tile.UnavailableTile;
import Packet.*;
import Character.Player;
import Character.Character;
import org.lwjgl.Sys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyHandler extends CharacterHandler
{

    public EnemyHandler(int enemyId) throws IOException {
        this.characterId = enemyId;
        Random rng = new Random();
        EnemyFactory factory = new EnemyFactory();
        int x, y;
        do {
            x = rng.nextInt(100);
            y = rng.nextInt(100);
        } while (Server.map.getTileByLoc(x, y).getClass() == UnavailableTile.class);
        characterModel = factory.createEnemy(11, x, y);

        //Randomly decide whether to assign artifact, and if yes - which one
        int artifactRoll = rng.nextInt(8);
        if (artifactRoll == 2 || artifactRoll == 3) {
            characterModel.setArtifact(new WarmQuartz());
        } else if (artifactRoll == 1) {
            characterModel.setArtifact(new MagicStaff());
        }

        characterModel.id = enemyId;
        Turnline.getInstance().Add(characterModel);
    }
    @Override
    public void run() {
        Turnline turnline = Turnline.getInstance();
        //synchronized (turnline)
        {
//            Server.Turnline turnline = Server.Turnline.getInstance();
            if ( turnline.getCharacter() != null && turnline.getCharacter() instanceof Enemy && turnline.getCharacter().id == characterId)
            {
//                turnline.Remove(characterModel);
                turnline.Next();
                PacketBuilder builder;
                //if(turnline.getCharacter() != null && turnline.getCharacter() instanceof Character.Player)
                {
                    //System.out.println("HP = "+ characterModel.getHP());

                    if(!Server.clients.isEmpty())
                    {
                        Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y()).setOnTile(null);
                        characterModel.updateCharacter(Server.clients.get(1).characterModel);
                        Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y()).setOnTile(characterModel);
                    }

                    List<Area> newAreas = Server.getAreas(characterModel.getRel_y(), characterModel.getRel_x());
                    List<Area> oldOnes = new ArrayList<>(this.areas);
                    for (int i = 0; i < this.areas.size(); i++){
                        this.areas.get(i).removeCharacter(this);
                    }
                    this.areas.removeAll(oldOnes);
                    this.areas.addAll(newAreas);
                    /**/
//                    System.out.println();
                    for(Area area : this.areas)
                        area.addCharacter(this);
                    /*oldOnes.removeAll(newAreas);
                    for (Map.Area area : oldOnes){
                        area.removeCharacter(this);
                    }
                    for (Map.Area area : newAreas){
                        area.addCharacter(this);
                    }*/
                    if(!Server.clients.isEmpty())
                    {
                        builder = new DamagePlayerPacketBuilder();
                        PacketDirector.constructDamagePlayerPacket(builder, (Player) Server.clients.get(1).characterModel);
                        Packet p = builder.getPacket();
                        try {
                            if(!Server.clients.isEmpty())
                                Server.clients.get(1).sendPacket(p);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        if(Server.clients.get(1).characterModel.getHP() <= 0)
                            turnline.Remove(Server.clients.get(1).characterModel);

                        builder = new ChangeOfEnemyPositionPacketBuilder();
                        PacketDirector.constructChangeOfEnemyPositionPacket(builder, (Enemy) characterModel);

                        Packet packet = builder.getPacket();
                        try {
                            Server.broadcastPacket(packet);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                   /* builder = new Packet.Builder.DamagePlayerPacketBuilder();
                    Packet.PacketDirector.constructDamagePlayerPacket(builder, (Character.Player)turnline.getCharacter());
                    Packet.Packet p = builder.getPacket();
                    /*Packet.Packet p = new Packet.Packet(-1, -1, -1, true);
                    p.setAttack(true);
                    p.setHP(turnline.getCharacter().getHP());*/



                }
                if (Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y()).getClass() == FieryTile.class) {
                    characterModel.damageCharacter();
                }
                characterModel.rollArtifactEffect();
                if(characterModel.getHP() <= 0)
                    turnline.Remove(characterModel);
            }
        }
    }
    @Override
    public void sendPacket(Packet packet) throws IOException {}
}
