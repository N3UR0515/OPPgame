package Character;


import Map.Map;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.GameContainer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


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

    @Before
    public void init() {
        map = new Map(100, 100);
        container = mock(GameContainer.class);
        player = mock(Player.class);
        when(player.getX()).thenReturn(0);
        when(player.getX()).thenReturn(0);
        when(container.getWidth()).thenReturn(10);
        when(container.getHeight()).thenReturn(10);
    }

    /**
     * Parasoft Jtest UTA: Test for updateCamera(GameContainer)
     *
     * @author otsob
     * @see Camera#updateCamera(GameContainer)
     */
    @Test
    public void testUpdateCamera() throws Throwable {
        Camera camera = new Camera(container, player);
        assertEquals(5, camera.getCameraX());
        assertEquals(5, camera.getCameraY());
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