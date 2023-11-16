package Packet.Builder;


import Packet.Packet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Parasoft Jtest UTA: Test class for DamagePlayerPacketBuilder
 *
 * @see DamagePlayerPacketBuilder
 * @author BP
 */
public class DamagePlayerPacketBuilderTest
{
    /**
     * Parasoft Jtest UTA: Test for getPacket()
     *
     * @author BP
     * @see DamagePlayerPacketBuilder#getPacket()
     */
    @Test(timeout = 5000)
    public void testGetPacket() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // Given
        DamagePlayerPacketBuilder underTest = new DamagePlayerPacketBuilder();
        underTest.setId(1);
        underTest.setHP(99);
        // When
        Packet result = underTest.getPacket();

        // Then - assertions for result of method getPacket()
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertFalse(result.isEnemy());
        assertTrue(result.isAttack());
        assertFalse(result.isSetHealth());
        assertEquals(99, result.getHP());

    }
}