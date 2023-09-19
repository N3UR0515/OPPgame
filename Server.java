import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 12345;
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

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastPlayerPositions() {
        synchronized (playerPositions) {
            String positions = playerPositions.stream()
                    .map(pp -> pp.getClientId() + ":" + pp.getX() + "," + pp.getY())
                    .reduce("", (acc, pos) -> acc + pos + ";");

            for (ClientHandler client : clients) {
                client.sendMessage(positions);
            }
        }
    }




    public static void storePlayerPosition(int clientId, int x, int y) {
        synchronized (playerPositions) {
            playerPositions.removeIf(pp -> pp.getClientId() == clientId);
            playerPositions.add(new PlayerPosition(clientId, x, y));
        }
    }

    public static void removeClientHandler(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private int clientId;
    private BufferedReader in;

    public ClientHandler(Socket clientSocket, int clientId) {
        this.clientSocket = clientSocket;
        this.clientId = clientId;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;

            while ((message = in.readLine()) != null) {
                //System.out.println("Received from client " + clientId + ": " + message);

                // Parse the received message (assuming it's in the format "X:Y")
                String[] parts = message.split(":");
                if (parts.length == 2) {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);

                    // Store the player's position in the server
                    Server.storePlayerPosition(clientId, x, y);

                    // Broadcast updated positions to all clients
                    Server.broadcastPlayerPositions();
                }
            }
        } catch (SocketException e) {
            // Handle socket disconnection here
            System.out.println("Client " + clientId + " disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close resources and remove the client handler
            try {
                out.close();
                clientSocket.close();
                Server.removeClientHandler(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void sendMessage(String message) {
        out.println(message);
    }
}
