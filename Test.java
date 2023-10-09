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
    private HashMap<Integer, Character> players = new HashMap<>();
    private HashMap<Integer, Character> enemies = new HashMap<>();
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
            PacketBuilder builder;
            if(player.getAttackTile() != null)
            {
                Tile tile = player.getAttackTile();
                builder = new PlayerAttackPacketBuilder();
                PacketDirector.constructPlayerAttackPacket(builder, tile);
                packet = builder.getPacket();
                player.endAttack();
            }
            else
            {
                builder = new ChangeOfPlayerPositionPacketBuilder();
                PacketDirector.constructChangeOfPlayerPositonPacket(builder, player);
                packet = builder.getPacket();
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
                if(packet.isAttack())
                {
                    PacketCommand command = new DamagePlayerPacketCommand();
                    command.execute(packet, players, map, camera);
                    player.setHP(packet.getHP());
                }
                else if(packet.isEnemy())
                {
                    PacketCommand command = new CharacterMovePacketCommand();
                    command.execute(packet, enemies, map, camera);
                }
                else
                {
                    PacketCommand command = new PlayerMovePacketCommand();
                    command.execute(packet, players, map, camera);
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

        for(Character p: players.values())
        {
            p.drawCharacter(g);
        }

        for(Character e: enemies.values())
        {
            e.drawCharacter(g);
        }

        player.drawCharacter(g);
        player.drawHealth(g);
    }
}
