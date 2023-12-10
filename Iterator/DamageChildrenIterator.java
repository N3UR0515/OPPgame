package Iterator;

import Server.CharacterHandler;
import Server.EnemyCompositeHandler;

import java.util.ArrayList;

public class DamageChildrenIterator implements IIterator{
    @Override
    public boolean hasNext(EnemyCompositeHandler object) {
        if(object.getClass() == EnemyCompositeHandler.class) {
            if(object.getChildren() != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<CharacterHandler> getNext(EnemyCompositeHandler object) {
        return object.getChildren();
    }

    @Override
    public void damageLastChildren(ArrayList<CharacterHandler> children) {
        for(CharacterHandler c: children ){
            if (hasNext((EnemyCompositeHandler) c)) {
                damageLastChildren(getNext((EnemyCompositeHandler) c));
            } else {
                c.characterModel.damageCharacter();
            }
        }
    }
}
