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
    private static List<ClientHandler> clients = new ArrayList<>();
    public static List<EnemyHandler> enemies = new ArrayList<>();
    public static Map map;
    public  static Turnline turnline;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and listening on port " + PORT);

            turnline = new Turnline();
            map = new Map(100, 100);

            int clientId = 1; // Initialize a unique identifier for clients
            //new Thread(Server::updateEnemy).start();
            enemies.add(new EnemyHandler(0));

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
            try {
                Thread.sleep(100);
            }  catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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


    public static void broadcastPlayerPositions() {
        //synchronized (playerPositions)
        {
            StringBuilder positions = new StringBuilder();
            for(ClientHandler client : clients)
            {
                positions.append(client.clientId).append(":").append(client.playerModel.getRel_x()).append(",").append(client.playerModel.getRel_y()).append(";");
            }
            /*String positions = playerPositions.stream()
                    //.filter(pp -> pp.changed)
                    .map(pp -> pp.getClientId() + ":" + pp.getX() + "," + pp.getY())
                    .reduce("", (acc, pos) -> acc + pos + ";");*/

            for (ClientHandler client : clients) {
                client.sendMessage(positions.toString());
            }
        }
    }
    public static void broadcastEnemyPositions() {
        //synchronized (enemy)
        {
            StringBuilder positions = new StringBuilder();
            for(EnemyHandler enemy:enemies)
            {
                positions.append("e").append(enemy.enemyId).append(":").append(enemy.enemyModel.getRel_x()).append(",").append(enemy.enemyModel.getRel_y()).append(";");
            }
//            String positions = playerPositions.stream()
//                    .filter(pp -> pp.changed)
//                    .map(pp -> pp.getClientId() + ":" + pp.getX() + "," + pp.getY())
//                    .reduce("", (acc, pos) -> acc + pos + ";");

            for (ClientHandler client : clients) {
                client.sendMessage(positions.toString());
            }
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
            Server.turnline.Add(playerModel);

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            out.writeObject(Server.map);
            out.writeObject(playerModel);

            //map = (Map) in.readObject();
            //System.out.println(map.getRows());
            //playerModel = (Player) in.readObject();
            //camera = (Camera) in.readObject();
            //enemy = new Enemy(10, map, camera);

            //new Thread(this::run).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            String player;
            if((player = (String) in.readObject()) != null)
            {
               System.out.println(player);
                if(Server.turnline.getCharacter() instanceof Player && Server.turnline.getCharacter().id == clientId)
                {
                    sendMessage("YOUR TURN");
                    String[] parts = player.split(":");
                    if(parts.length == 2)
                    {
                        int x = Integer.parseInt(parts[0]);
                        int y = Integer.parseInt(parts[1]);



                        playerModel.setRel_x(x);
                        playerModel.setRel_y(y);

                        Server.broadcastPlayerPositions();
                        //enemy.updateEnemy(playerModel);
                        //Server.broadcastEnemyPositions();

                    }
                    System.out.println("help");
                    Server.turnline.Add(Server.enemies.get(0).enemyModel);
                    Server.turnline.Next();

                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush(); // Flush the buffered output to ensure it's sent immediately
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
