package Packet;


import AbstractFactory.Monster.Witch;
import Character.Enemies.Enemy;
import Map.Map;
import Character.Character;
import Character.Player;
import Character.Camera;
import Packet.Command.*;
import org.junit.Before;
import org.junit.Test;


import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


/**
 * Parasoft Jtest UTA: Test class for CommandInvoker
 *
 * @see CommandInvoker
 * @author BP
 */
public class CommandInvokerTest
{
    Map map;
    int cols;
    int rows;
    Camera camera;
    Enemy enemy;
    Player player;
    Packet packet;
    HashMap<Integer, Character> characters;
    @Before
    public void init() {
        cols = 100;
        rows = 100;
        map = new Map(rows, cols);
        player = new Player(10, map, 10, 10);
        camera = mock(Camera.class);
        enemy = new Witch(9, map, 9, 9);
        packet = mock(Packet.class);
        characters = new HashMap<>();
    }
    /**
     * Parasoft Jtest UTA: Test for CommandInvoker
     *
     * @author BP
     * @see CommandInvoker
     */
    @Test(timeout = 60000)
    public void testCommandInvoker() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // Given
        CommandInvoker underTest = new CommandInvoker();
        assertNull(underTest.getCommand());

        PacketCommand enemyMove = new CharacterMovePacketCommand(packet, characters, map, camera);
        PacketCommand playerMove = new PlayerMovePacketCommand(packet, characters, map, camera);
        PacketCommand damagePlayer = new DamagePlayerPacketCommand(packet, characters, map, camera);
        PacketCommand tileUpdate = new MapTileUpdateCommand(packet, characters, map, camera);
        // When
        underTest.setCommand(enemyMove);
        assertNull(underTest.getCommand());

        underTest.setCommand(playerMove);
        assertNull(underTest.getCommand());

        underTest.setCommand(damagePlayer);
        assertNull(underTest.getCommand());

        underTest.setCommand(tileUpdate);
        assertNull(underTest.getCommand());

        underTest.invoke();
        assertEquals(enemyMove, underTest.getCommand());

        underTest.invoke();
        assertEquals(playerMove, underTest.getCommand());

        underTest.invoke();
        assertEquals(damagePlayer, underTest.getCommand());

        underTest.invoke();
        assertEquals(tileUpdate, underTest.getCommand());

        underTest.undo();
        assertEquals(tileUpdate, underTest.getCommand());

        underTest.undo();
        assertEquals(damagePlayer, underTest.getCommand());

        underTest.undo();
        assertEquals(playerMove, underTest.getCommand());

        underTest.undo();
        assertEquals(enemyMove, underTest.getCommand());

        underTest.undo();
        assertEquals(enemyMove, underTest.getCommand());

        underTest.redo();
        assertEquals(enemyMove, underTest.getCommand());

        underTest.redo();
        assertEquals(playerMove, underTest.getCommand());

        underTest.redo();
        assertEquals(damagePlayer, underTest.getCommand());

        underTest.redo();
        assertEquals(tileUpdate, underTest.getCommand());

        underTest.redo();
        assertEquals(tileUpdate, underTest.getCommand());

        for (int i = 0; i < 3; i++) {
            underTest.undo();
        }
        underTest.setCommand(enemyMove);
        underTest.invoke();
        assertEquals(enemyMove, underTest.getCommand());
    }

    /**
     * Parasoft Jtest UTA: Test for getResults()
     *
     * @author BP
     * @see CommandInvoker#getResults()
     */
    @Test(timeout = 5000)
    public void testGetResults() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // Given
        CommandInvoker underTest = new CommandInvoker();

        // When
        HashMap<Integer, Character> result = underTest.getResults();
        assertNull(result);

        PacketCommand enemyMove = new CharacterMovePacketCommand(packet, characters, map, camera);
        underTest.setCommand(enemyMove);
        underTest.invoke();

        result = underTest.getResults();
        assertNotNull(result);
    }
}