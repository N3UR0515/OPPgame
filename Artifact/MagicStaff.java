package Artifact;

import Character.Character;

public class MagicStaff extends Artifact{
    public MagicStaff() {
        super();
    }

    @Override
    public void rollForEffect(Character character, int HPCap) {

        if (rng.nextInt(90) == 7){
            if (character.getHP() != HPCap){
                character.setHP(HPCap);
            }
        }
    }
}
