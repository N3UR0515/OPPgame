import Tile.FieryTile;
import org.lwjgl.Sys;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 12345;
    public static List<ClientHandler> clients = new ArrayList<>();
    public static List<EnemyHandler> enemies = new ArrayList<>();
    public static Map map;

    public static void main(String[] args) {
        try {

            map = new Map(100, 100);

            int clientId = 1; // Initialize a unique identifier for clients

            enemies.add(new EnemyHandler(0));

            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and listening on port " + PORT);

            new Thread(Server::Turns).start();


            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, clientId);
                clients.add(clientHandler);


                clientId++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Turns()
    {
        while(true)
        {
            /*try {
                Thread.sleep(1000);
            }  catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
            Turnline turnline = Turnline.getInstance();
            if(turnline.getCharacter() != null)
            {
                Character character = turnline.getCharacter();
                System.out.println(character.id);
                if(character instanceof Player)
                {
                    for(ClientHandler client : clients)
                    {
                        if(client.clientId == character.id)
                        {
                            client.run();
                            break;
                        }
                    }
                }
                else if(character instanceof Enemy)
                {
                    for(EnemyHandler enemy : enemies)
                    {
                        if(enemy.enemyId == character.id)
                        {
                            enemy.run();
                            break;
                        }
                    }
                }
            }

        }
    }

    public static void broadcastPacket(Packet packet) throws IOException {
        for(ClientHandler client : clients)
        {
            client.sendPacket(packet);
        }
    }

}

class ClientHandler implements Runnable {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public int clientId;
    public Player playerModel;

    public ClientHandler(Socket clientSocket, int clientId) {
        this.clientId = clientId;
        try {
            playerModel = new Player(10, Server.map, 0, 0);
            playerModel.id = clientId;
            Turnline.getInstance().Add(playerModel);

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            out.writeObject(Server.map);
            out.writeObject(playerModel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        try
        {
            Packet packet;
            PacketBuilder builder;
            if((packet = (Packet) in.readObject()) != null)
            {
                Turnline turnline = Turnline.getInstance();
                if(turnline.getCharacter() instanceof Player && turnline.getCharacter().id == clientId)
                {
                    if(!packet.isAttack())
                    {
                        playerModel.setRel_x(packet.getX());
                        playerModel.setRel_y(packet.getY());

                        builder = new ChangeOfPlayerPositionPacketBuilder();
                        PacketDirector.constructChangeOfPlayerPositonPacket(builder, playerModel);
                        Packet outPacket = builder.getPacket();
                        Server.broadcastPacket(outPacket);
                    }
                    else
                    {
                        for(EnemyHandler enemy : Server.enemies)
                        {
                            Enemy e = enemy.enemyModel;
                            if(e.rel_y == packet.getY() && e.rel_x == packet.getX())
                            {
                                enemy.enemyModel.damageCharacter();
                                System.out.println(enemy.enemyModel.getHP());
                                if(enemy.enemyModel.getHP() <= 0)
                                {
                                    turnline.Remove(enemy.enemyModel);
                                }
                                break;
                            }
                        }

                        for(ClientHandler client : Server.clients)
                        {
                            Player p = client.playerModel;
                            if(p.rel_y == packet.getY() && p.rel_x == packet.getX())
                            {
                                client.playerModel.damageCharacter();
                                builder = new DamagePlayerPacketBuilder();
                                PacketDirector.constructDamagePlayerPacket(builder, client.playerModel);
                                Packet pa = builder.getPacket();
                                /*Packet pa = new Packet(-1, -1, -1, false);
                                pa.setAttack(true);
                                pa.setHP(client.playerModel.getHP());*/
                                client.sendPacket(pa);
                                if(client.playerModel.getHP() <= 0)
                                {
                                    turnline.Remove(client.playerModel);
                                }
                                break;
                            }
                        }
                    }
                    //Checking if player is on a fiery tile. If yes - send damage packet to that player
                    if (Server.map.getTileByLoc(playerModel.getRel_x(), playerModel.getRel_y()).getClass() == FieryTile.class){
                        playerModel.damageCharacter();
                        PacketBuilder dmgBuilder = new DamagePlayerPacketBuilder();
                        PacketDirector.constructDamagePlayerPacket(dmgBuilder, playerModel);
                        Packet toSend = dmgBuilder.getPacket();
                        sendPacket(toSend);
                    }

                    if(Server.enemies.get(0).enemyModel.getHP() > 0)
                        turnline.Add(Server.enemies.get(0).enemyModel);
                    turnline.Next();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPacket(Packet packet) throws IOException {
        out.writeObject(packet);
        out.flush();
    }
}
