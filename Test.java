import org.lwjgl.Sys;
import org.newdawn.slick.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Test extends BasicGame {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private List<PlayerPosition> otherPlayerPositions = new ArrayList<>();
    private final Object playerPositionsLock = new Object(); // Lock for accessing otherPlayerPositions
    private Map map;
    private Player player;
    private Camera camera;
    private Enemy enemy;
    private String oldPlayerPosition = "-1:-1\n";
    public boolean MyTurn = true;

    public Test() {
        super("Game");
    }

    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new Test());
            app.setDisplayMode(800, 600, false);
            app.setShowFPS(true);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void init(GameContainer container) throws SlickException {

        //player = new Player(10, map, 0, 0);
        //camera = new Camera(container, player);

        //map = new Map(0, 0);

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            map = (Map) in.readObject();
            player = (Player) in.readObject();
            camera = new Camera(container, player);
            enemy = new Enemy(10, map, 0, 1, camera);

            //out.writeObject(map);
            //out.writeObject(player);
            //out.writeObject(camera);

            new Thread(this::Send).start();
            new Thread(this::Receive).start();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void Send()
    {
        try {
            //while(true)
            {
                if(!oldPlayerPosition.equals(player.getRel_x() + ":" + player.getRel_y()))
                {
                    out.writeObject(player.getRel_x() + ":" + player.getRel_y());
                    oldPlayerPosition = player.getRel_x() + ":" + player.getRel_y();
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Receive()
    {
        try {
            String players;
            while((players = (String) in.readObject()) != null && !players.isEmpty())
            {
                System.out.println(players);
                if(players.startsWith("e")) {
                    String[] es = players.split(";");
                    for(String e: es)
                    {
                        String[] parts = e.split(":");
                        int enemyId = Integer.parseInt(parts[0].substring(1));
                        String[] coords = parts[1].split(",");
                        int x = Integer.parseInt(coords[0]);
                        int y = Integer.parseInt(coords[1]);
                        enemy.setRel_x(x);
                        enemy.setRel_y(y);
                        //enemy.updateCharacter(new Player(10, map, x, y));

                    }

                } else if (players.equals("YOUR TURN")) {
                    System.out.println("My Turn");
                    MyTurn = true;

                } else {
                    String[] ps = players.split(";");
                    for (String p : ps) {
                        String[] parts = p.split(":");
                        int clientId = Integer.parseInt(parts[0]);
                        String[] coords = parts[1].split(",");
                        int x = Integer.parseInt(coords[0]);
                        int y = Integer.parseInt(coords[1]);
                        //otherPlayerPositions.removeIf(pp -> pp.getClientId() == clientId);
                        //otherPlayerPositions.add(new PlayerPosition(clientId, x, y, true));

                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    public void update(GameContainer container, int delta) throws SlickException {
        //if(MyTurn)
        {
            if (player.updateCharacter(container))
            {
                new Thread(this::Send).start();
                MyTurn = false;
            };
        }

        camera.updateCamera(container);
        enemy.setRel_y(enemy.rel_y);
        enemy.setRel_x(enemy.rel_x);
        //enemy.updateEnemy(player);
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        map.drawMap(g, camera);

        synchronized (playerPositionsLock) {
            for (PlayerPosition pp : otherPlayerPositions) {
                Player otherPlayer = new Player(10, map, pp.getX(), pp.getY());
                otherPlayer.drawCharacter(g);
            }
        }

        player.drawCharacter(g);
        enemy.drawCharacter(g);
    }
}
