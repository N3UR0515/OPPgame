package Packet.Builder;


import Packet.Packet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Parasoft Jtest UTA: Test class for HealthPickUpSetPacketBuilder
 *
 * @see HealthPickUpSetPacketBuilder
 * @author BP
 */
public class HealthPickUpSetPacketBuilderTest
{
    /**
     * Parasoft Jtest UTA: Test for getPacket()
     *
     * @author BP
     * @see HealthPickUpSetPacketBuilder#getPacket()
     */
    @Test(timeout = 5000)
    public void testGetPacket() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // Given
        HealthPickUpSetPacketBuilder underTest = new HealthPickUpSetPacketBuilder();
        underTest.setX(54);
        underTest.setY(54);
        // When
        Packet result = underTest.getPacket();

        // Then - assertions for result of method getPacket()
        assertNotNull(result);
        assertEquals(0, result.getId());
        assertEquals(54, result.getX());
        assertEquals(54, result.getY());
        assertFalse(result.isEnemy());
        assertFalse(result.isAttack());
        assertTrue(result.isSetHealth());
//        assertEquals(-10, result.getHP());

    }
}