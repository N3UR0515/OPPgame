package Packet.Builder;


import Packet.Packet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Parasoft Jtest UTA: Test class for PlayerAttackPacketBuilder
 *
 * @see PlayerAttackPacketBuilder
 * @author BP
 */
public class PlayerAttackPacketBuilderTest
{
    /**
     * Parasoft Jtest UTA: Test for getPacket()
     *
     * @author BP
     * @see PlayerAttackPacketBuilder#getPacket()
     */
    @Test(timeout = 5000)
    public void testGetPacket() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // Given
        PlayerAttackPacketBuilder underTest = new PlayerAttackPacketBuilder();
        underTest.setX(12);
        underTest.setY(12);

        // When
        Packet result = underTest.getPacket();

        // Then - assertions for result of method getPacket()
        assertNotNull(result);
        assertEquals(0, result.getId());
        assertEquals(12, result.getX());
        assertEquals(12, result.getY());
        assertFalse(result.isEnemy());
        assertTrue(result.isAttack());
        assertFalse(result.isSetHealth());
        assertEquals(-10, result.getHP());

    }
}