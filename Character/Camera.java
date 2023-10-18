package Character;

import org.newdawn.slick.GameContainer;

import java.io.Serializable;

public class Camera implements Serializable {
    Player player;
    int cameraX;
    int cameraY;
    public Camera(GameContainer container, Player player)
    {
        this.player = player;
        updateCamera(container);

    }

    public void updateCamera(GameContainer container)
    {
        int x = player.getX();
        int y = player.getY();
        cameraX = (int) (container.getWidth() / 2 - x);
        cameraY = (int) (container.getHeight() / 2 - y);
    }

    public int getCameraX() {
        return cameraX;
    }

    public void setCameraX(int cameraX) {
        this.cameraX = cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public void setCameraY(int cameraY) {
        this.cameraY = cameraY;
    }
}
