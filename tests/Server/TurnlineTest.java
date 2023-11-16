package Server;

import Server.Turnline;
import Character.Player;
import Map.Map;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class TurnlineTest {

    Map map;
    Player character;
    Player character2;
    @Before
    public void init()
    {
        map = new Map(100, 100);
        character = new Player(10, map, 0, 0);
        character.id = 1;

        character2 = new Player(10, map, 0, 0);
        character2.id = 2;
    }

    @Test
    public void testGetInstance() {
        Turnline instance1 = Turnline.getInstance();
        Turnline instance2 = Turnline.getInstance();

        assertSame(instance1, instance2);
    }

    @Test
    public void testRemove() {
        Turnline turnline = Turnline.getInstance();

        turnline.Add(character);
        turnline.Remove(character);
        turnline.Remove(character2);


        assertNull(turnline.getCharacter());
    }

    @Test
    public void testAdd() {
        Turnline turnline = Turnline.getInstance();


        turnline.Add(character);

        assertEquals(character.id, turnline.getCharacter().id);
    }

    @Test
    public void testNext() {
        Turnline turnline = Turnline.getInstance();

        turnline.Add(character);
        turnline.Add(character2);

        turnline.Next();

        assertEquals(character.id, turnline.getCharacter().id);
    }

    @Test
    public void testGetCharacter() {
        Turnline turnline = Turnline.getInstance();

        turnline.Add(character);

        assertEquals(character, turnline.getCharacter());
    }

}
