package AbstractFactory;

import AbstractFactory.Monster.*;
import Character.Enemies.Enemy;
import Map.Map;
import Server.Server;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MonsterFactoryParametrizedTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"WALKING", Walker.class},
                {"walking", Walker.class},
                {"WaLkIng", Walker.class},
                {"CRAWLING", Crawler.class},
                {"crawling", Crawler.class},
                {"CraWLinG", Crawler.class},
                {"RUNNING", Runner.class},
                {"running", Runner.class},
                {"Running", Runner.class},
                {"fsdsd", Runner.class},
                {"efefe", Runner.class},
                {"Splitter", Runner.class}
        });
    }

    Map underTest;
    int cols;
    int rows;
    private String input;
    private Class<?> expectedResult;
    public MonsterFactoryParametrizedTest(String input, Class<?> expectedResult) {
        this.input = input;
        this.expectedResult = expectedResult;
        cols = 100;
        rows = 100;
        Server.map = new Map(cols, rows);
    }
    @Test
    public void testMonsterCreation() {
        EnemyFactory factory = new MonsterFactory();
        Enemy createdMonster = factory.GetEnemy(input,0,0);
        Assert.assertNotNull(createdMonster);
        Assert.assertEquals(expectedResult, createdMonster.getClass());
    }
}

