package Effects;
import Character.Character;

public class BleedingOutEffect extends Effect{
    @Override
    public void affect(Character character) {
        int maxHp = character.getMaxHP();
        int Hp = character.getHP();

        float perct = (float) Hp / maxHp;
        if(perct <= 0.5)
        {
            character.damageCharacter();
        }
        if(perct <= 0.25)
        {
            character.damageCharacter();
        }
        if(perct <= 0.1)
        {
            character.damageCharacter();
        }

        affectNext(character);
    }
}
