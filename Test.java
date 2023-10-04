import org.lwjgl.Sys;
import org.newdawn.slick.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test extends BasicGame {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private HashMap<Integer, Player> players = new HashMap<>();
    private HashMap<Integer, Enemy> enemies = new HashMap<>();
    private Map map;
    private Player player;
    private Camera camera;
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

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            map = (Map) in.readObject();
            player = (Player) in.readObject();
            camera = new Camera(container, player);

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
        try
        {
            Packet packet;
            if(player.getAttackTile() != null)
            {
                Tile tile = player.getAttackTile();
                packet = new Packet(0, tile.getTrel_x(), tile.getTrel_y(), false);
                packet.isAttack = true;
                player.endAttack();
            }
            else
            {
                packet = new Packet(0, player.getRel_x(), player.getRel_y(), false);
            }
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void Receive() {
        Packet packet;
        try{
            while((packet = (Packet)in.readObject()) != null)
            {
                if(packet.isAttack)
                {
                    player.setHP(packet.HP);
                }
                else if(packet.isEnemy)
                {
                    if(enemies.containsKey(packet.id))
                    {
                        Enemy temp = enemies.get(packet.id);
                        temp.setRel_y(packet.y);
                        temp.setRel_x(packet.x);
                        enemies.replace(packet.id, temp);
                    }
                    else
                    {
                        enemies.put(packet.id, new Zombie(10, map, packet.x, packet.y, camera));
                    }
                }
                else
                {
                    if(players.containsKey(packet.id))
                    {
                        Player temp = players.get(packet.id);
                        temp.setRel_y(packet.y);
                        temp.setRel_x(packet.x);
                        players.replace(packet.id, temp);
                    }
                    else
                    {
                        players.put(packet.id, new Player(10, map, packet.x, packet.y));
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
            if (player.updateCharacter(container))
            {
                new Thread(this::Send).start();
                MyTurn = false;
            };

        camera.updateCamera(container);
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        map.drawMap(g, camera);

        for(Player p: players.values())
        {
            p.drawCharacter(g);
        }

        for(Enemy e: enemies.values())
        {
            e.drawCharacter(g);
        }

        player.drawCharacter(g);
        player.drawHealth(g);
    }
}
