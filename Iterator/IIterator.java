package Iterator;

import Server.CharacterHandler;
import Server.EnemyCompositeHandler;

import java.util.ArrayList;

public interface IIterator {
    public boolean hasNext(EnemyCompositeHandler object);
    public ArrayList<CharacterHandler> getNext(EnemyCompositeHandler object);
    public void damageLastChildren(ArrayList<CharacterHandler> children);
}
