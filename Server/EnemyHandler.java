package Server;

import Character.Enemies.Enemy;
import Character.Enemies.EnemyFactory;
import Map.Area;
import Packet.Builder.ChangeOfEnemyPositionPacketBuilder;
import Packet.Builder.DamagePlayerPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.Packet;
import Map.Tile.FieryTile;
import Packet.*;
import Character.Player;
import Character.Character;

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
        characterModel = factory.createEnemy(11, rng.nextInt(100), rng.nextInt(100));
        characterModel.id = enemyId;
        Turnline.getInstance().Add(characterModel);
    }
    @Override
    public void run() {
        Turnline turnline = Turnline.getInstance();
        synchronized (turnline)
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
                        characterModel.updateCharacter(Server.clients.get(0).characterModel);

                    List<Area> newAreas = Server.map.getAreas(characterModel.getY(), characterModel.getX());
                    List<Area> oldOnes = new ArrayList<>(this.areas);
                   // System.out.println(this.areas.size() + " areas");
                    this.areas.removeAll(oldOnes);
                    this.areas.addAll(newAreas);
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
                        PacketDirector.constructDamagePlayerPacket(builder, (Player) Server.clients.get(0).characterModel);
                        Packet p = builder.getPacket();
                        try {
                            if(!Server.clients.isEmpty())
                                Server.clients.get(0).sendPacket(p);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        if(Server.clients.get(0).characterModel.getHP() <= 0)
                            turnline.Remove(Server.clients.get(0).characterModel);

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
                    if(characterModel.getHP() < 0)
                        characterModel.setHP(0);
                }
            }
        }
    }
    @Override
    public void sendPacket(Packet packet) throws IOException {}
}
