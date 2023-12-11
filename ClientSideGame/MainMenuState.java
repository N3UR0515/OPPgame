package ClientSideGame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class MainMenuState extends BasicGameState {

    private StateBasedGame game;

    public MainMenuState(int state, StateBasedGame game) {
        this.game = game;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.setColor(new Color(0xa8f255));
        g.fillRect(0, 0, gc.getWidth(), gc.getHeight());

        g.setColor(Color.black);
        g.drawString("Insert name", gc.getWidth() / 2 - 40, 50);

        g.drawString("Press \"1\" to start matchmaking", gc.getWidth() / 2 - 130, 150);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    }

    @Override
    public void keyPressed(int key, char c) {
        // Check if the key pressed is '1' (Start Game)
        if (key == Input.KEY_1) {
            // Switch to the GameplayState
            game.enterState(Game.MatchmakingState);
        }
    }

    @Override
    public int getID() {
        return Game.MainMenuState;
    }
}

