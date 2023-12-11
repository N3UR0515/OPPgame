import Character.Enemies.Enemy;
import CharacterDecorator.CharacterWithHealthBar;
import CharacterDecorator.CharacterWithLowHP;
import CharacterDecorator.UIElement;
import Effects.*;
import Logs.PacketSenderProxy;
import Map.Map;
import Packet.Builder.ChangeOfPlayerPositionPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.Builder.PlayerAttackPacketBuilder;
import Packet.*;
import Map.Tile.*;
import Packet.Command.*;
import org.newdawn.slick.*;
import Character.*;
import Character.Character;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
//xpzbwvhmfftufnfmoe@cazlp.com
//j2_qjotH7g1Ug36h

public class Test extends BasicGame {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private ObjectOutputStream out;
    private PacketSenderProxy packetSenderProxy;
    private ObjectInputStream in;
    //private HashMap<Integer, Character> players = new HashMap<>();
    //private HashMap<Integer, Character> enemies = new HashMap<>();

    private HashMap<Integer, Character> characters = new HashMap<>();
    private Map map;
    private Player player;
    private Camera camera;
    public boolean MyTurn = true;
    private CommandInvoker invoker;
    Effect effect;

    public Test() {
        super("Game");
    }

    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new Test());
            app.setDisplayMode(640, 480, false);
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
            packetSenderProxy = new PacketSenderProxy(out);
            in = new ObjectInputStream(socket.getInputStream());

            map = (Map) in.readObject();
            //player = (Player) in.readObject();
            int id = (int) in.readObject();
            player = new Player(10, map, 0, 0);
            player.id = id;
            camera = new Camera(container, player);
            invoker = new CommandInvoker();
            effect = Effect.link(new AttackingEffect(), new BleedingEffect(), new GetHitEffect());

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
        effect.affect(player);
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
        packetSenderProxy.sendPacket(packet);
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

            invoker.invoke();
            if(characters.get(player.id) != null)
                player.setHP(characters.get(player.id).getHP());
            if(container.getInput().isKeyPressed(Input.KEY_P))
                invoker.undo();
            if(container.getInput().isKeyPressed(Input.KEY_R))
                invoker.redo();

        camera.updateCamera(container);
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        map.drawMap(g, camera);
        UIElement element;

        for(Character c : characters.values()) {
            if(c instanceof Enemy)  {
                element = c;
                element.drawCharacter(g, 0, 0, c.getHP());
            } else {
                element = new CharacterWithHealthBar(player);
                if (player.getHP()<3){
                    element = new CharacterWithLowHP(element);
                    element.drawCharacter(g, 0, 0, player.getHP());
                }
            }
            c.affectSelf();
            element = c;
            element.drawCharacter(g, 0, 0, c.getHP());
        }
        element = player;
        element = new CharacterWithHealthBar(player);
        if (player.getHP()<3){
            element = new CharacterWithLowHP(element);
        }
        element.drawCharacter(g, 0, 0, player.getHP());
    }
}
