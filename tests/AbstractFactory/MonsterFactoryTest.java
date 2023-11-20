package AbstractFactory;


import AbstractFactory.Monster.*;
import Character.Enemies.Enemy;
import Map.Map;
import Server.Server;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Parasoft Jtest UTA: Test class for MonsterFactory
 *
 * @see MonsterFactory
 * @author BP
 */
public class MonsterFactoryTest
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
    public void testMonsterCreation() throws Throwable {
        EnemyFactory factory = new MonsterFactory();
        Enemy walk = factory.GetEnemy("WALKING", 0, 0);
        assertEquals(walk.getClass(), Walker.class);
        Enemy crawl = factory.GetEnemy("CrAwLiNg", 1, 1);
        assertEquals(crawl.getClass(), Crawler.class);
        Enemy run = factory.GetEnemy("spliTTer", 4, 4);
        assertEquals(run.getClass(), Runner.class);
    }
}