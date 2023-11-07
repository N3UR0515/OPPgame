package Artifact;

import Character.Character;

public class WarmQuartz extends Artifact{
    public WarmQuartz() {
        super();
    }

    @Override
    public void rollForEffect(Character character, int HPCap) {
        int hp = character.getHP();
        if (rng.nextInt(26) == 7){
            hp++;
            if (hp <= HPCap) {
                character.setHP(hp);
            }
        }
    }
}
