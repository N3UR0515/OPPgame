package ClientSideGame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class EndgameState extends BasicGameState {

    private StateBasedGame game;

    public EndgameState(int state, StateBasedGame game) {
        this.game = game;
    }

    @Override
    public int getID() {
        return Game.EndgameState;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
    }

    @Override
    public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.setColor(new Color(0xa8f255));
        g.fillRect(0, 0, gc.getWidth(), gc.getHeight());

        g.setColor(Color.black);
        g.drawString("Game over", gc.getWidth() / 2 - 40, 50);

        g.drawString("Press \"1\" to start matchmaking.\n" +
                "Press \"2\" to enter main menu.\n" +
                "Press \"3\" to exit game.", gc.getWidth() / 2 - 130, 150);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_1) {
            game.enterState(Game.MatchmakingState);
        } else if (key == Input.KEY_2) {
            // Switch to the GameplayState
            game.enterState(Game.MainMenuState);
        } else if (key == Input.KEY_3) {
            System.exit(0);
        }
    }
}
