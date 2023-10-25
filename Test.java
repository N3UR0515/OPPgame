import Map.Map;
import Packet.Builder.ChangeOfPlayerPositionPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.Builder.PlayerAttackPacketBuilder;
import Packet.*;
import Map.Tile.*;
import Packet.Command.*;
import PickUp.PickUpStore;
import org.newdawn.slick.*;
import Character.*;
import Character.Character;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class Test extends BasicGame {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    //private HashMap<Integer, Character> players = new HashMap<>();
    //private HashMap<Integer, Character> enemies = new HashMap<>();

    private HashMap<Integer, Character> characters = new HashMap<>();
    private Map map;
    private Player player;
    private Camera camera;
    public boolean MyTurn = true;
    private CommandInvoker invoker;

    public Test() {
        super("Game");
    }

    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new Test());
            app.setDisplayMode(640, 480, true);
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
            //player = (Player) in.readObject();
            int id = (int) in.readObject();
            player = new Player(10, map, 0, 0);
            player.id = id;
            camera = new Camera(container, player);
            invoker = new CommandInvoker();

            //new Thread(this::Send).start();
            Send();
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
        PacketCommand command;
        try{
            while((packet = (Packet)in.readObject()) != null)
            {
                if(packet.isAttack())
                {
                    invoker.setCommand(new DamagePlayerPacketCommand(packet, characters, map, camera));
                }
                else if(packet.isEnemy())
                {
                    invoker.setCommand(new CharacterMovePacketCommand(packet, characters, map, camera));
                }
                else if(packet.isSetHealth())
                {
                    invoker.setCommand(new MapTileUpdateCommand(packet, characters, map, camera));
                }
                else
                {
                    invoker.setCommand(new PlayerMovePacketCommand(packet, characters, map, camera));
                }
                invoker.invoke();


                //characters = invoker.getResults();
                if(characters.get(player.id) != null)
                    player.setHP(characters.get(player.id).getHP());

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
            if(container.getInput().isKeyPressed(Input.KEY_P))
                invoker.undo();
            if(container.getInput().isKeyPressed(Input.KEY_R))
                invoker.redo();

        camera.updateCamera(container);
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        map.drawMap(g, camera);

        for(Character c : characters.values())
            c.drawCharacter(g);

        player.drawCharacter(g);
        player.drawHealth(g);
    }
}
