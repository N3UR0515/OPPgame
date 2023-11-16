package Artifact;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import Character.Player;

import Map.Map;


import Character.Character;
import org.junit.Test;

public class ArtifactTest {
    @Test
    public void testMagicStaffRollForEffect() {
        MagicStaff magicStaff = new MagicStaff();

        Map map = new Map(100, 100);

        Player character = new Player(10, map, 0, 0);
        character.damageCharacter();
        character.damageCharacter();
        character.damageCharacter();

        character.setArtifact(magicStaff);
        for(int i = 0; i < 1000; i++)
            character.rollArtifactEffect();

        assertEquals(10, character.getHP());
    }

    @Test
    public void testWarmQuartzRollForEffect() {
        WarmQuartz warmQuartz = new WarmQuartz();

        Map map = new Map(100, 100);

        Player character = new Player(10, map, 0, 0);
        character.damageCharacter();
        character.damageCharacter();
        character.damageCharacter();

        character.setArtifact(warmQuartz);

        for(int i = 0; i < 26; i++)
            character.rollArtifactEffect();


        assertEquals(8, character.getHP());


    }

}
