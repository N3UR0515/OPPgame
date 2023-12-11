package ClientSideGame;

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
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class Game extends StateBasedGame {
    public static final int MainMenuState = 0;
    public static final int MatchmakingState = 1;
    public static final int GameplayState = 2;
    public static final int EndgameState = 3;
    private Player player;
    private final String SERVER_IP = "localhost";
    private final int SERVER_PORT = 12345;

    private Socket socket;
    private ObjectOutputStream out;
    private PacketSenderProxy packetSenderProxy;
    private ObjectInputStream in;

    private HashMap<Integer, Character> characters = new HashMap<>();
    private Map map;
    private Camera camera;
    public boolean MyTurn = true;
    private CommandInvoker invoker;
    Effect effect;

    public Game() {
        super("Game");
    }

    public String getSERVER_IP() {
        return SERVER_IP;
    }

    public int getSERVER_PORT() {
        return SERVER_PORT;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public PacketSenderProxy getPacketSenderProxy() {
        return packetSenderProxy;
    }

    public void setPacketSenderProxy(PacketSenderProxy packetSenderProxy) {
        this.packetSenderProxy = packetSenderProxy;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public HashMap<Integer, Character> getCharacters() {
        return characters;
    }

    public void setCharacters(HashMap<Integer, Character> characters) {
        this.characters = characters;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public boolean isMyTurn() {
        return MyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        MyTurn = myTurn;
    }

    public CommandInvoker getInvoker() {
        return invoker;
    }

    public void setInvoker(CommandInvoker invoker) {
        this.invoker = invoker;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new Game());
            app.setDisplayMode(640, 480, false);
            app.setShowFPS(true);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        addState(new MainMenuState(MainMenuState, this));
        addState(new MatchmakingState(MatchmakingState, this));
        addState(new GameplayState(GameplayState, this));
        addState(new EndgameState(EndgameState, this));
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

}
