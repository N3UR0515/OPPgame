package AbstractFactory;

import AbstractFactory.Monster.*;
import Character.Enemies.Enemy;
import Map.Map;
import Server.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MutantFactoryParametrizedTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"BOMBER", Bomber.class},
                {"bomber", Bomber.class},
                {"BomBer", Bomber.class},
                {"SPITTER", Spitter.class},
                {"spitter", Spitter.class},
                {"SpiTTer", Spitter.class},
                {"WITCH", Witch.class},
                {"witch", Witch.class},
                {"Witch", Witch.class},
                {"efefe", Witch.class},
                {"Splitter", Witch.class}
        });
    }

    Map underTest;
    int cols;
    int rows;
    private String input;
    private Class<?> expectedResult;
    public MutantFactoryParametrizedTest(String input, Class<?> expectedResult) {
        this.input = input;
        this.expectedResult = expectedResult;
        cols = 100;
        rows = 100;
        Server.map = new Map(cols, rows);
    }
    @Test
    public void testMutantCreation() {
        EnemyFactory factory = new MutantFactory();
        Enemy createdMutant = factory.GetEnemy(input,0,0);
        Assert.assertNotNull(createdMutant);
        Assert.assertEquals(expectedResult, createdMutant.getClass());
    }
}
