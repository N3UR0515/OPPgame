package Packet;


import AbstractFactory.Monster.Walker;
import Character.Character;
import Character.Player;
import Character.Enemies.Enemy;
import Map.Map;
import Map.Tile.FieryTile;
import Map.Tile.Tile;
import Packet.Builder.*;
import Packet.Builder.PacketBuilder;
import org.junit.Assert;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;


/**
 * Parasoft Jtest UTA: Test class for PacketDirector
 *
 * @see PacketDirector
 * @author BP
 */
public class PacketDirectorTest
{
    Map underTest;
    int cols;
    int rows;
    @Before
    public void init() {
        cols = 100;
        rows = 100;
        underTest = new Map(rows, cols);
    }
    @Test
    public void testChangeEnemyPositionPacket() throws Throwable{
        PacketBuilder builder = new ChangeOfEnemyPositionPacketBuilder();
        Enemy character = new Walker(10, underTest, 10, 10);
        PacketDirector.constructChangeOfEnemyPositionPacket(builder, character);
        Packet packet = builder.getPacket();
        assertEquals(packet.getId(), character.id);
        assertEquals(packet.getX(), character.getRel_x());
        assertEquals(packet.getY(), character.getRel_y());
        assertFalse(packet.isAttack());
        assertTrue(packet.isEnemy());
        assertEquals(packet.getHP(), character.getHP());
    }

    @Test
    public void testChangePlayerPositionPacket() throws Throwable{
        PacketBuilder builder = new ChangeOfPlayerPositionPacketBuilder();
        Character character = new Player(10, underTest, 10, 10);
        PacketDirector.constructChangeOfPlayerPositonPacket(builder, character);
        Packet packet = builder.getPacket();
        assertEquals(packet.getId(), character.id);
        assertEquals(packet.getX(), character.getRel_x());
        assertEquals(packet.getY(), character.getRel_y());
        assertFalse(packet.isAttack());
        assertFalse(packet.isEnemy());
    }

    @Test
    public void testDamagePlayerPacket() throws Throwable{
        PacketBuilder builder = new DamagePlayerPacketBuilder();
        Character character = new Player(10, underTest, 10, 10);
        PacketDirector.constructDamagePlayerPacket(builder, (Player) character);
        Packet packet = builder.getPacket();
        assertEquals(packet.getId(), character.id);
        assertEquals(packet.getHP(), character.getHP());
        assertTrue(packet.isAttack());
    }

    @Test
    public void testPlayerAttackPacket() throws Throwable{
        PacketBuilder builder = new PlayerAttackPacketBuilder();
        Tile tile = new FieryTile(true, 0,0,0,0,"");
        PacketDirector.constructPlayerAttackPacket(builder, tile);
        Packet packet = builder.getPacket();
        assertEquals(packet.getX(), tile.getTrel_x());
        assertEquals(packet.getY(), tile.getTrel_y());
        assertTrue(packet.isAttack());
    }

    @Test
    public void testHealthPickupPacket() throws Throwable{
        PacketBuilder builder = new HealthPickUpSetPacketBuilder();
        Tile tile = new FieryTile(true, 0,0,0,0,"");
        PacketDirector.constructSetHealthPickupPacket(builder, tile);
        Packet packet = builder.getPacket();
        assertEquals(packet.getX(), tile.getTrel_x());
        assertEquals(packet.getY(), tile.getTrel_y());
        assertTrue(packet.isSetHealth());
    }
}