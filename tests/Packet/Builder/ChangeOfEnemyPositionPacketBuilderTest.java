package Packet.Builder;


import Packet.Packet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Parasoft Jtest UTA: Test class for ChangeOfEnemyPositionPacketBuilder
 *
 * @see ChangeOfEnemyPositionPacketBuilder
 * @author BP
 */
public class ChangeOfEnemyPositionPacketBuilderTest
{
    /**
     * Parasoft Jtest UTA: Test for getPacket()
     *
     * @author BP
     * @see ChangeOfEnemyPositionPacketBuilder#getPacket()
     */
    @Test(timeout = 5000)
    public void testGetPacket() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // Given
        ChangeOfEnemyPositionPacketBuilder underTest = new ChangeOfEnemyPositionPacketBuilder();
        underTest.setId(4);
        underTest.setY(4);
        underTest.setX(4);
        underTest.setHP(4);
        // When
        Packet result = underTest.getPacket();

        // Then - assertions for result of method getPacket()
        assertNotNull(result);
        assertEquals(4, result.getId());
        assertEquals(4, result.getX());
        assertEquals(4, result.getY());
        assertTrue(result.isEnemy());
        assertFalse(result.isAttack());
        assertEquals(4, result.getHP());

    }
}