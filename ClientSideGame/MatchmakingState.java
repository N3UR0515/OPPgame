package ClientSideGame;

import Character.Player;
import Logs.PacketSenderProxy;
import Map.Map;
import Packet.PacketDirector;
import Packet.Builder.ChangeOfPlayerPositionPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.Packet;
import Server.Server;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MatchmakingState extends BasicGameState {

    private Game game;
//    private static final String SERVER_IP = "localhost";
//    private static final int SERVER_PORT = 12345;
//    private Socket socket;
    private Player player;
//    private ObjectOutputStream out;
//    private PacketSenderProxy packetSenderProxy;
//    private ObjectInputStream in;

    public MatchmakingState(int state, Game game){
        this.game = game;
    }

    @Override
    public int getID() {
        return Game.MatchmakingState;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        try {
            this.game.setSocket(new Socket(this.game.getSERVER_IP(), this.game.getSERVER_PORT()));
            this.game.setOut(new ObjectOutputStream(this.game.getSocket().getOutputStream()));
            this.game.setPacketSenderProxy(new PacketSenderProxy(this.game.getOut()));
            this.game.setIn(new ObjectInputStream(this.game.getSocket().getInputStream()));

            Map map = (Map) this.game.getIn().readObject();
            this.game.setMap(map);
            int id = (int) this.game.getIn().readObject();
            player = new Player(10, map, 0, 0);
            player.id = id;
            this.game.setPlayer(player);

            Send();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void Send() {
        Packet packet;
        PacketBuilder builder = new ChangeOfPlayerPositionPacketBuilder();
        PacketDirector.constructChangeOfPlayerPositonPacket(builder, player);
        packet = builder.getPacket();
        game.getPacketSenderProxy().sendPacket(packet);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.setColor(new Color(0xa8f255));
        g.fillRect(0, 0, gc.getWidth(), gc.getHeight());

        g.setColor(Color.black);
        g.drawString("Waiting for more players", gc.getWidth() / 2 - 100, 90);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        if (Server.clients.size() >= 2) {
            game.enterState(Game.GameplayState);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE){
            Server.removeClient(player.id);
            game.enterState(Game.MainMenuState);
        } else if (key == Input.KEY_COMMA) {
            game.enterState(Game.GameplayState);
        }
    }
}
