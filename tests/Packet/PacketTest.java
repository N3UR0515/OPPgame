package Packet;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


/**
 * Parasoft Jtest UTA: Test class for Packet
 *
 * @see Packet
 * @author BP
 */
public class PacketTest
{
    /**
     * Parasoft Jtest UTA: Test for getHP()
     *
     * @author BP
     * @see Packet#getHP()
     */
    @Test(timeout = 5000)
    public void testGetHP() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // Given
        Packet underTest = new Packet();
        // When
        int result = underTest.getHP();

        // Then - assertions for result of method getHP()
        assertEquals(-10, result);

    }
}