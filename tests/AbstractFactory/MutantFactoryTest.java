package AbstractFactory;


import AbstractFactory.Monster.Bomber;
import AbstractFactory.Monster.Spitter;
import AbstractFactory.Monster.Witch;
import Character.Enemies.Enemy;
import Map.Map;
import Server.Server;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


/**
 * Parasoft Jtest UTA: Test class for MutantFactory
 *
 * @see MutantFactory
 * @author BP
 */
public class MutantFactoryTest
{
    Map underTest;
    int cols;
    int rows;

    @Before
    public void init() {
        cols = 100;
        rows = 100;
        Server.map = new Map(cols, rows);
    }

    @Test
    public void testMutantCreation() throws Throwable {
        EnemyFactory factory = new MutantFactory();
        Enemy bomber = factory.GetEnemy("bomber", 0, 0);
        assertEquals(bomber.getClass(), Bomber.class);
        Enemy spitter = factory.GetEnemy("spiTTer", 1, 1);
        assertEquals(spitter.getClass(), Spitter.class);
        Enemy witch = factory.GetEnemy("spliTTer", 4, 4);
        assertEquals(witch.getClass(), Witch.class);
    }
}