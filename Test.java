import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Test extends BasicGame {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private List<PlayerPosition> otherPlayerPositions = new ArrayList<>();
    private final Object playerPositionsLock = new Object(); // Lock for accessing otherPlayerPositions
    private Map map;
    private Player player;
    private Camera camera;
    private Enemy enemy;

    public Test() {
        super("Game");
    }

    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new Test());
            app.setDisplayMode(1920, 1080, false);
            app.setShowFPS(true);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void init(GameContainer container) throws SlickException {
        int numCols = 1000 / 150;
        int numRows = 1000 / 150;
        map = new Map(numCols, numRows);
        player = new Player(10, map);
        camera = new Camera(container, player);
        enemy = new Enemy(10, map, camera);

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(GameContainer container, int delta) throws SlickException {
        player.updatePlayer(container);
        camera.updateCamera(container);
        enemy.updateEnemy(player);

        synchronized (playerPositionsLock) {
            out.println(player.getRel_x() + ":" + player.getRel_y());

            try {
                String message;
                if((message = in.readLine()) != null) {
                    String[] players = message.split(";");
                    for (String p : players) {
                        String[] parts = p.split(":");
                        int clientId = Integer.parseInt(parts[0]);
                        String[] coords = parts[1].split(",");
                        int x = Integer.parseInt(coords[0]);
                        int y = Integer.parseInt(coords[1]);
                        otherPlayerPositions.removeIf(pp -> pp.getClientId() == clientId);
                        otherPlayerPositions.add(new PlayerPosition(clientId, x, y));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        map.drawMap(g, camera);

        synchronized (playerPositionsLock) {
            for (PlayerPosition pp : otherPlayerPositions) {
                int x = pp.getX();
                int y = pp.getY();
                Tile tile = map.getTileByLoc(x, y);
                g.setColor(Color.green);
                g.fill(new Rectangle(tile.getX() + camera.cameraX, tile.getY()+ camera.cameraY, 50, 50));
            }
        }

        player.drawPlayer(g);
        enemy.drawEnemy(g);
    }
}
