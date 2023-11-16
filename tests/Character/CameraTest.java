package Character;


import Map.Map;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.GameContainer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


/**
 * Parasoft Jtest UTA: Test class for Camera
 *
 * @author otsob
 * @see Camera
 */
public class CameraTest {

    Map map;
    GameContainer container;
    Player player;
    int playerX;
    int playerY;

    @Before
    public void init() {
        map = new Map(100, 100);
        container = mock(GameContainer.class);
        player = new Player(18, map, 4, 5);
        playerX = player.getX();
        playerY = player.getY();
    }

    /**
     * Parasoft Jtest UTA: Test for updateCamera(GameContainer)
     *
     * @author otsob
     * @see Camera#updateCamera(GameContainer)
     */
    @Test
    public void testUpdateCamera() throws Throwable {
        int expectedX = (int) (container.getWidth() / 2 - playerX);
        int expectedY = (int) (container.getHeight() / 2 - playerY);
        Camera camera = new Camera(container, player);
        assertEquals(expectedX, camera.getCameraX());
        assertEquals(expectedY, camera.getCameraY());
    }

    /**
     * Parasoft Jtest UTA: Test for getCameraX()
     *
     * @author BP
     * @see Camera#getCameraX()
     */
    @Test(timeout = 5000)
    public void testSetCameraX() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        Camera camera = new Camera(container, player);
        camera.setCameraX(151);
        camera.setCameraY(15818);
        assertEquals(151, camera.getCameraX());
        assertEquals(15818, camera.getCameraY());
    }
}