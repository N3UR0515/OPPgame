import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.Sys;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class Test extends BasicGame {
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
        // Initialize the checkerboard pattern
        int numCols = /*container.getWidth()*/ 1000 / 150;
        int numRows = /*container.getHeight()*/ 1000 / 150;
        map = new Map(numCols, numRows);
        player = new Player(10, map);
        camera = new Camera(container, player);
        enemy = new Enemy(10, map, camera);
    }


    public void update(GameContainer container, int delta) throws SlickException {
        player.updatePlayer(container);
        camera.updateCamera(container);
        enemy.updateEnemy(player);
    }


    public void render(GameContainer container, Graphics g) throws SlickException {
        map.drawMap(g, camera);
        player.drawPlayer(g);
        enemy.drawEnemy(g);
    }
}
