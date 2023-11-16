package Map;

import Server.CharacterHandler;
import Server.ClientHandler;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.Sys;
import Character.Player;
import Server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class AreaTest {
    Area area;
    CharacterHandler characterHandler;
    Player player;
    Map map;
    Server server;
    @Before
    public void init()
    {
        server = new Server();
        map = new Map(100, 100);
        area = new Area();
        player = new Player(10, map, 0, 0);
        player.setHP(10);
        characterHandler = mock(ClientHandler.class);
    }

    @Test
    public void testAddCharacter()
    {
        area.addCharacter(characterHandler);
        assertEquals(area.characters.size(), 1);
    }

    @Test
    public void testRemoveCharacter()
    {
        area.addCharacter(characterHandler);
        area.removeCharacter(characterHandler);
        assertEquals(area.characters.size(), 0);
    }

    @Test
    public void testSendAttackToAlreadyAttackedPlayer() throws IOException {
        List<CharacterHandler> damagedOnes = new ArrayList<>();
        characterHandler.characterModel = player;
        int hp1 = characterHandler.characterModel.getHP();
        area.addCharacter(characterHandler);
        damagedOnes.add(characterHandler);
        area.sendAttack(0, 0, damagedOnes);
        int hp2 = characterHandler.characterModel.getHP();
        assertEquals(hp1, hp2);
    }

    @Test
    public void testSendAttackToPlayerThatWasNotAttacked() throws IOException {
        List<CharacterHandler> damagedOnes = new ArrayList<>();
        characterHandler.characterModel = player;
        int hp1 = characterHandler.characterModel.getHP();
        System.out.println(hp1);
        area.addCharacter(characterHandler);
        area.sendAttack(0, 0, damagedOnes);
        int hp2 = characterHandler.characterModel.getHP();
        assertEquals(hp1-1, hp2);
    }
}
