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
    private static List<PlayerPosition> playerPositions = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();
    public static Map map;
    public static Enemy enemy;
    public  static Turnline turnline;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and listening on port " + PORT);

            turnline = new Turnline();
            map = new Map(100, 100);
            enemy = new Enemy(10, map, 0, 1);

            int clientId = 1; // Initialize a unique identifier for clients
            new Thread(Server::updateEnemy).start();

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


    public static void broadcastPlayerPositions() {
        synchronized (playerPositions)
        {
            String positions = playerPositions.stream()
                    //.filter(pp -> pp.changed)
                    .map(pp -> pp.getClientId() + ":" + pp.getX() + "," + pp.getY())
                    .reduce("", (acc, pos) -> acc + pos + ";");

            for (ClientHandler client : clients) {
                client.sendMessage(positions);
            }
        }
    }
    public static void broadcastEnemyPositions() {
        synchronized (enemy) {
//            String positions = playerPositions.stream()
//                    .filter(pp -> pp.changed)
//                    .map(pp -> pp.getClientId() + ":" + pp.getX() + "," + pp.getY())
//                    .reduce("", (acc, pos) -> acc + pos + ";");

            for (ClientHandler client : clients) {
                client.sendMessage("e" + enemy.getRel_x() + ":" + enemy.getRel_y());
            }
        }
    }




    public static void storePlayerPosition(int clientId, int x, int y) {
        synchronized (playerPositions) {
            boolean found = false;

            for (PlayerPosition pp : playerPositions) {
                if (pp.getClientId() == clientId) {
                    // Check if x and y have changed
                    if (pp.getX() != x || pp.getY() != y) {
                        pp.setX(x);
                        pp.setY(y);
                        pp.changed = true;
                    }
                    else
                    {
                        pp.changed = false;
                    }
                    found = true;
                    break; // No need to continue searching
                }
            }

            // If no matching player position was found, add a new one
            if (!found) {
                playerPositions.add(new PlayerPosition(clientId, x, y, true));
            }
        }
    }

    public static void storePlayer(Player player)
    {
        synchronized (players)
        {
            players.add(player);
        }
    }
    public static void updatePlayers(int clientId, int x, int y)
    {
        synchronized (players)
        {
            for(Player player:players)
            {
                if (player.id == clientId)
                {
                    player.setRel_x(x);
                    player.setRel_y(y);
                }
            }
        }
    }

    private static void updateEnemy()
    {
        while (true)
        {
            //System.out.println(playerPositions.size());
            if(!playerPositions.isEmpty())
            {
                enemy.updateCharacter(new Player(10, map, playerPositions.get(0).getX(), playerPositions.get(0).getY()));
                //broadcastEnemyPositions();
            }
        }


    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int clientId;
    private Player playerModel;
    private boolean isPlayerTurn = false;

    public ClientHandler(Socket clientSocket, int clientId) {
        this.clientSocket = clientSocket;
        this.clientId = clientId;
        try {
            playerModel = new Player(10, Server.map, 0, 0);
            playerModel.id = clientId;
            Server.turnline.Add(playerModel);
            Server.storePlayer(playerModel);
            Server.storePlayerPosition(clientId, 0, 0);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            out.writeObject(Server.map);
            out.writeObject(playerModel);

            //map = (Map) in.readObject();
            //System.out.println(map.getRows());
            //playerModel = (Player) in.readObject();
            //camera = (Camera) in.readObject();
            //enemy = new Enemy(10, map, camera);
            new Thread(this::run).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            String player;
            while((player = (String) in.readObject()) != null)
            {
               System.out.println(player);
                if(Server.turnline.getCharacter().id == clientId)
                {
                    sendMessage("YOUR TURN");
                    String[] parts = player.split(":");
                    if(parts.length == 2)
                    {
                        int x = Integer.parseInt(parts[0]);
                        int y = Integer.parseInt(parts[1]);
                        Server.storePlayerPosition(clientId, x, y);
                        Server.broadcastPlayerPositions();

                        playerModel.setRel_x(x);
                        playerModel.setRel_y(y);
                        Server.updatePlayers(clientId, x, y);
                        //enemy.updateEnemy(playerModel);
                        //Server.broadcastEnemyPositions();
                    }
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
