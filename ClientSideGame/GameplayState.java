package ClientSideGame;

import Character.Enemies.Enemy;
import CharacterDecorator.CharacterWithHealthBar;
import CharacterDecorator.CharacterWithLowHP;
import CharacterDecorator.UIElement;
import Effects.AttackingEffect;
import Effects.BleedingEffect;
import Effects.Effect;
import Effects.GetHitEffect;
import Character.Camera;
import Character.Character;
import Packet.Command.*;
import Packet.PacketDirector;
import Map.Tile.Tile;
import Packet.Builder.ChangeOfPlayerPositionPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.Builder.PlayerAttackPacketBuilder;
import Packet.CommandInvoker;
import Packet.Packet;
import Server.Server;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameplayState extends BasicGameState {

    private Game game;

    public GameplayState(int state, Game game){
        this.game = game;
    }

    @Override
    public int getID() {
        return Game.GameplayState;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        this.game.setCamera(new Camera(container, this.game.getPlayer()));
        this.game.setInvoker(new CommandInvoker());
        this.game.setEffect(Effect.link(new AttackingEffect(), new BleedingEffect(), new GetHitEffect()));
        Send();
        new Thread(this::Receive).start();

    }

    public void Receive() {
        Packet packet;
        PacketCommand command;
        try{
            while((packet = (Packet)this.game.getIn().readObject()) != null)
            {
                if(packet.isAttack())
                {
                    this.game.getInvoker().setCommand(new DamagePlayerPacketCommand(packet, this.game.getCharacters(), this.game.getMap(), this.game.getCamera()));
                }
                else if(packet.isEnemy())
                {
                    this.game.getInvoker().setCommand(new CharacterMovePacketCommand(packet, this.game.getCharacters(), this.game.getMap(), this.game.getCamera()));
                }
                else if(packet.isSetHealth())
                {
                    this.game.getInvoker().setCommand(new MapTileUpdateCommand(packet, this.game.getCharacters(), this.game.getMap(), this.game.getCamera()));
                }
                else
                {
                    this.game.getInvoker().setCommand(new PlayerMovePacketCommand(packet, this.game.getCharacters(), this.game.getMap(), this.game.getCamera()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void Send()
    {
        this.game.getEffect().affect(this.game.getPlayer());
        Packet packet;
        PacketBuilder builder;
        if(this.game.getPlayer().getAttackTile() != null)
        {
            Tile tile = this.game.getPlayer().getAttackTile();
            builder = new PlayerAttackPacketBuilder();
            PacketDirector.constructPlayerAttackPacket(builder, tile);
            packet = builder.getPacket();
            this.game.getPlayer().endAttack();
        }
        else
        {
            builder = new ChangeOfPlayerPositionPacketBuilder();
            PacketDirector.constructChangeOfPlayerPositonPacket(builder, this.game.getPlayer());
            packet = builder.getPacket();
        }
        this.game.getPacketSenderProxy().sendPacket(packet);
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void render(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        this.game.getMap().drawMap(g, this.game.getCamera());
        UIElement element;

        for(Character c : this.game.getCharacters().values()) {
            if(c instanceof Enemy)  {
                element = c;
                element.drawCharacter(g, 0, 0, c.getHP());
            } else {
                element = new CharacterWithHealthBar(this.game.getPlayer());
                if (this.game.getPlayer().getHP()<3){
                    element = new CharacterWithLowHP(element);
                    element.drawCharacter(g, 0, 0, this.game.getPlayer().getHP());
                }
            }
            c.affectSelf();
            element = c;
            element.drawCharacter(g, 0, 0, c.getHP());
        }
        element = this.game.getPlayer();
        element = new CharacterWithHealthBar(this.game.getPlayer());
        if (this.game.getPlayer().getHP()<3){
            element = new CharacterWithLowHP(element);
        }
        element.drawCharacter(g, 0, 0, this.game.getPlayer().getHP());
    }

    @Override
    public void update(GameContainer container, StateBasedGame stateBasedGame, int i) throws SlickException {
        if (this.game.getPlayer().updateCharacter(container))
        {
            new Thread(this::Send).start();
            this.game.setMyTurn(false);
        };

        this.game.getInvoker().invoke();
        if(this.game.getCharacters().get(this.game.getPlayer().id) != null)
            this.game.getPlayer().setHP(this.game.getCharacters().get(this.game.getPlayer().id).getHP());
        while(container.getInput().isKeyDown(Input.KEY_P))
            this.game.getInvoker().undo();
        while (container.getInput().isKeyDown(Input.KEY_R))
            this.game.getInvoker().redo();

        this.game.getCamera().updateCamera(container);
        if (this.game.getPlayer().getHP() <= 0)
        {
            game.enterState(Game.EndgameState);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_LALT){
            game.enterState(Game.MainMenuState);
        }
    }
}
