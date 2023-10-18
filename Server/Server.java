package Server;

import Map.Map;
import Map.Area;
import Packet.Builder.ChangeOfPlayerPositionPacketBuilder;
import Packet.Builder.DamagePlayerPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.Packet;
import Map.Tile.FieryTile;
import Character.Character;
import Character.Player;
import Character.Enemies.Enemy;
import Packet.PacketDirector;
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

            EnemyHandler temp = new EnemyHandler(0);
            List<Area> areas = Server.map.getAreas(temp.characterModel.getRel_y(), temp.characterModel.getRel_x());
            for (Area area : areas){
                area.addCharacter(temp);
            }
            enemies.add(temp);


            int random =10;
            for (int i = 0; i < random;i++){
                EnemyHandler temp1 = new EnemyHandler(i+100);
                List<Area> areas1 = Server.map.getAreas(temp1.characterModel.getRel_y(), temp1.characterModel.getRel_x());
                for(Area area : areas1){
                    area.addCharacter(temp1);
                }
                enemies.add(temp1);
            }


            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server.Server is running and listening on port " + PORT);

            new Thread(Server::Turns).start();


            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, clientId);
                areas = Server.map.getAreas(temp.characterModel.getRel_y(), temp.characterModel.getRel_x());
                for (Area area : areas){
                    area.addCharacter(clientHandler);
                }
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
//            try {
//                Thread.sleep(1000);
//            }  catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            Turnline turnline = Turnline.getInstance();
            if(turnline.getCharacter() != null)
            {
                Character character = turnline.getCharacter();
                System.out.println(character.id);
                System.out.println(character.getHP() + " HP");
                if(character instanceof Player)
                {
                    for(ClientHandler client : clients)
                    {
                        if(client.characterId == character.id)
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
                        if(enemy.characterId == character.id)
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

