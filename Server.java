import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 12345;
    private static final int MAX_CLIENTS = 10;
    private static List<ClientHandler> clients = new ArrayList<>();
    private static List<PlayerPosition> playerPositions = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and listening on port " + PORT);

            int clientId = 1; // Initialize a unique identifier for clients

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
        synchronized (playerPositions) {
            String positions = playerPositions.stream()
                    .filter(pp -> pp.changed)
                    .map(pp -> pp.getClientId() + ":" + pp.getX() + "," + pp.getY())
                    .reduce("", (acc, pos) -> acc + pos + ";");

            for (ClientHandler client : clients) {
                client.sendMessage(positions);
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
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int clientId;
    private Map map;
    private Player player;
    private Camera camera;
    private Enemy enemy;

    public ClientHandler(Socket clientSocket, int clientId) {
        this.clientSocket = clientSocket;
        this.clientId = clientId;
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            map = (Map) in.readObject();
            player = (Player) in.readObject();
            camera = (Camera) in.readObject();
            Enemy enemy = new Enemy(10, map, camera);
            enemy.updateEnemy(player);
            new Thread(this::run).start();
        } catch (IOException | ClassNotFoundException e) {
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
               String[] parts = player.split(":");
               if(parts.length == 2)
               {
                   int x = Integer.parseInt(parts[0]);
                   int y = Integer.parseInt(parts[1]);
                   Server.storePlayerPosition(clientId, x, y);
                   Server.broadcastPlayerPositions();
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
