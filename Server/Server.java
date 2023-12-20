package Server;

import Interpreter.Context;
import Interpreter.Expression;
import Map.Map;
import Map.Area;
import Map.Tile.Tile;
import Packet.Packet;
import Character.Character;
import Character.Player;
import Character.Enemies.Enemy;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import Interpreter.StartServerExpression;
import Interpreter.StopServerExpression;
import Interpreter.RestartServerExpression;

public class Server {
    private static final int PORT = 12345;
    //public static List<ClientHandler> clients = new ArrayList<>();
    //public static List<EnemyHandler> enemies = new ArrayList<>();

    public static HashMap<Integer, ClientHandler> clients = new HashMap<>();
    public static HashMap<Integer, CharacterHandler> enemies = new HashMap<>();
    public static Map map;
    public static Map initMap;
    static Area[] areas;
    public static List<Tile> tiles;
    private static boolean running = true;
    private static ServerSocket serverSocket;
    private static Thread serverThread;
    public static void main(String[] args) {
        new Thread(Server::handleConsoleCommands).start();

        serverThread = new Thread(Server::initializeServer);
        serverThread.start();
    }
    public static void startServer() {
        if (!running) {
            running = true;
            serverThread = new Thread(Server::initializeServer);
            serverThread.start();
            System.out.println("Server started.");
        } else {
            System.out.println("Server is already running.");
        }
    }

    public static void stopServer() {
        if (!running){
            return;
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                running = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serverThread != null) {
            serverThread.interrupt();
        }
        System.out.println("Server stopped.");
    }

    private static void initializeServer() {
        try {
            map = new Map(100, 100);
            initMap = map.copy();
            tiles = map.generateHealthTiles();

            areas = new Area[10];
            for (int i = 0; i < 10;  i++){
                areas[i] = new Area();
            }

            int clientId = 1; // Initialize a unique identifier for clients

            EnemyCompositeHandler temp = new EnemyCompositeHandler(2);
            List<Area> areas = getAreas(temp.characterModel.getRel_y(), temp.characterModel.getRel_x());
            for (Area area : areas){
                area.addCharacter(temp);
            }
            enemies.put(temp.characterId, temp);


            int random =1;
            for (int i = 0; i < random;i++){
                EnemyHandler temp1 = new EnemyHandler(i+100);
                List<Area> areas1 = getAreas(temp1.characterModel.getRel_y(), temp1.characterModel.getRel_x());
                for(Area area : areas1){
                    area.addCharacter(temp1);
                }
                temp.add(temp1);
                temp1.setParent(temp);
                enemies.put(temp1.characterId, temp1);
            }


            serverSocket = new ServerSocket(PORT);
            System.out.println("Server.Server is running and listening on port " + PORT);

            new Thread(Server::Turns).start();


            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, clientId);
                areas = getAreas(temp.characterModel.getRel_y(), temp.characterModel.getRel_x());
                for (Area area : areas){
                    area.addCharacter(clientHandler);
                }

                clients.put(clientHandler.characterId, clientHandler);


                clientId++;

            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            } else {
                System.out.println("Server stopped.");
            }
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
                //System.out.println(character.id);
                //System.out.println(character.getHP() + " HP");
                if(character instanceof Player && clients.get(character.id) != null)
                {
                    synchronized (turnline) {
                        clients.get(character.id).run();
                    }
                }
                else if(character instanceof Enemy && enemies.get(character.id) != null)
                {
                    synchronized (turnline) {
                        enemies.get(character.id).run();
                    }
                }
            }

        }
    }

    public static void broadcastPacket(Packet packet) throws IOException {
        for(ClientHandler client : clients.values())
        {
            client.sendPacket(packet);
        }
    }

    //get areas for which specific coordinate (row and column) belongs to
    public static List<Area> getAreas(int row, int column){
        //Some tiles belong to few coordinates, so a list to get them all
        List<Area> temp = new ArrayList<>();
        if (column < 57) {
            if (row < 27) {
//                System.out.println(areas[0] + " 55555");
                temp.add(areas[0]);
            }
            if (row > 12 && row < 47) {
                temp.add(areas[2]);
            }
            if (row > 32 && row < 67) {
                temp.add(areas[4]);
            }
            if (row > 52 && row < 87) {
                temp.add(areas[6]);
            }
            if (row > 72) {
                temp.add(areas[8]);
            }
        }
        if (column > 42){
            if (row < 27) {
                temp.add(areas[1]);
            }
            if (row > 12 && row < 47) {
                temp.add(areas[3]);
            }
            if (row > 32 && row < 67) {
                temp.add(areas[5]);
            }
            if (row > 52 && row < 87) {
                temp.add(areas[7]);
            }
            if (row > 72) {
                temp.add(areas[9]);
            }
        }
        return temp;
    }

    public static void removeClient(int clientID){
        clients.remove(clientID);
    }

    private static void handleConsoleCommands() {
        try (Scanner scanner = new Scanner(System.in)) {
            Context context = new Context();

            while (true) {
                System.out.println("Enter command (start, stop, restart):");
                String command = scanner.nextLine();

                Expression expression = getExpression(command);
                if (expression != null) {
                    expression.interpret(context);
                } else {
                    System.out.println("Invalid command.");
                }
            }
        }
    }

    private static Expression getExpression(String command) {
        return switch (command.toLowerCase()) {
            case "start" -> new StartServerExpression();
            case "stop" -> new StopServerExpression();
            case "restart" -> new RestartServerExpression(new StopServerExpression(), new StartServerExpression());
            default -> {
                System.out.println("Command not recognized");
                yield null;
            }
        };
    }
}

