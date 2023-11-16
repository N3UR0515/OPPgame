package Packet.Builder;


import Packet.Packet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


/**
 * Parasoft Jtest UTA: Test class for ChangeOfPlayerPositionPacketBuilder
 *
 * @see ChangeOfPlayerPositionPacketBuilder
 * @author BP
 */
public class ChangeOfPlayerPositionPacketBuilderTest
{
    /**
     * Parasoft Jtest UTA: Test for getPacket()
     *
     * @author BP
     * @see ChangeOfPlayerPositionPacketBuilder#getPacket()
     */
    @Test(timeout = 5000)
    public void testGetPacket() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // Given
        ChangeOfPlayerPositionPacketBuilder underTest = new ChangeOfPlayerPositionPacketBuilder();
        underTest.setId(99);
        underTest.setY(99);
        underTest.setX(99);
        // When
        Packet result = underTest.getPacket();

        // Then - assertions for result of method getPacket()
        assertNotNull(result);
        assertEquals(99, result.getId());
        assertEquals(99, result.getX());
        assertEquals(99, result.getY());
        assertFalse(result.isEnemy());
        assertFalse(result.isAttack());

    }
}