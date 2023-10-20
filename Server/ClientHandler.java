package Server;

import Map.Area;
import Map.Tile.FieryTile;
import Packet.Builder.ChangeOfPlayerPositionPacketBuilder;
import Packet.Builder.DamagePlayerPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.Packet;
import Character.Player;
import Packet.PacketDirector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends CharacterHandler {
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket clientSocket, int clientId) {
        this.characterId = clientId;
        try {
            characterModel = new Player(100, Server.map, 0, 0);
            characterModel.id = clientId;
            Turnline.getInstance().Add(characterModel);

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            out.writeObject(Server.map);
            out.writeObject(characterModel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Turnline turnline = Turnline.getInstance();
        synchronized (turnline) {
            try {
                Packet packet;
                PacketBuilder builder;
                if ((packet = (Packet) in.readObject()) != null) {
//                Server.Turnline turnline = Server.Turnline.getInstance();
                    if (turnline.getCharacter() instanceof Player && turnline.getCharacter().id == characterId) {
                        if (!packet.isAttack()) {
                            characterModel.setRel_x(packet.getX());
                            characterModel.setRel_y(packet.getY());
                            List<Area> newAreas = Server.getAreas(packet.getY(), packet.getX());
                            List<Area> oldOnes = new ArrayList<>(this.areas);
                            this.areas.removeAll(oldOnes);
                            this.areas.addAll(newAreas);
                           /* for(Area area: oldOnes)
                                area.removeCharacter(this);*/
                            System.out.println("-----");
                            for(Area area : this.areas)
                            {
                                area.addCharacter(this);
                                System.out.println(area);
                            }



                            builder = new ChangeOfPlayerPositionPacketBuilder();
                            PacketDirector.constructChangeOfPlayerPositonPacket(builder, (Player) characterModel);
                            Packet outPacket = builder.getPacket();
                            Server.broadcastPacket(outPacket);
                        } else {
                            List<Area> areas = Server.getAreas(packet.getY(), packet.getX());
                            List<CharacterHandler> damagedOnes = new ArrayList<>();
                            for (Area area : areas) {
                                area.sendAttack(packet.getX(), packet.getY(), damagedOnes);
                            }

//                        for(Server.EnemyHandler enemy : Server.Server.enemies)//area
//                        {
//                            Character.Character.Enemies.Enemy e = enemy.enemyModel;
//                            if(e.rel_y == packet.getY() && e.rel_x == packet.getX())
//                            {
//                                enemy.enemyModel.damageCharacter();
//                                System.out.println(enemy.enemyModel.getHP());
//                                if(enemy.enemyModel.getHP() <= 0)
//                                {
//                                    turnline.Remove(enemy.enemyModel);
//                                }
//                                break;
//                            }
//                        }
//
//                        for(Server.ClientHandler client : Server.Server.clients)
//                        {
//                            Character.Player p = client.playerModel;
//                            if(p.rel_y == packet.getY() && p.rel_x == packet.getX())
//                            {
//                                client.playerModel.damageCharacter();
//                                builder = new Packet.Builder.DamagePlayerPacketBuilder();
//                                Packet.PacketDirector.constructDamagePlayerPacket(builder, client.playerModel);
//                                Packet.Packet pa = builder.getPacket();
//                                client.sendPacket(pa);
//                                if(client.playerModel.getHP() <= 0)
//                                {
//                                    turnline.Remove(client.playerModel);
//                                }
//                                break;
//                            }
//                        }
                        }
                        //Checking if player is on a fiery tile. If yes - send damage packet to that player
                        if (Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y()).getClass() == FieryTile.class) {
                            characterModel.damageCharacter();
                            PacketBuilder dmgBuilder = new DamagePlayerPacketBuilder();
                            PacketDirector.constructDamagePlayerPacket(dmgBuilder, (Player) characterModel);
                            Packet toSend = dmgBuilder.getPacket();
                            sendPacket(toSend);
                        }
//                    if(Server.Server.enemies.get(0).characterModel.getHP() > 0){
//                        turnline.Add(Server.Server.enemies.get(0).characterModel);
//                    }

                        turnline.Next();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void sendPacket(Packet packet) throws IOException {
        out.writeObject(packet);
        out.flush();
    }
}
