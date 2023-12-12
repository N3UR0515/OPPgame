package Iterator;

import Server.CharacterHandler;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AreaIterator implements IObjectIterator<ArrayList<CharacterHandler>>{

    private int defaultX;
    private int defaultY;
    private int counter;

    public AreaIterator() {}

    @Override
    public boolean hasNext(ArrayList<CharacterHandler> object) {
        return counter < object.size();
    }

    @Override
    public Object getNext(ArrayList<CharacterHandler> object) {
        return object.get(counter);
    }

    @Override
    public Object find(ArrayList<CharacterHandler> data) {
        ArrayList<CharacterHandler> characters = new ArrayList<>();
        for (Object obj : data) {
            if (obj instanceof CharacterHandler) {
                characters.add((CharacterHandler) obj);
            }
        }

        Collections.sort(characters, new Comparator<CharacterHandler>() {
            @Override
            public int compare(CharacterHandler o1, CharacterHandler o2) {
                double product1 = Math.sqrt(Math.pow(o1.characterModel.getRel_x() - defaultX, 2) + Math.pow(o1.characterModel.getRel_x() - defaultY, 2));
                double product2 = Math.sqrt(Math.pow(o2.characterModel.getRel_x() - defaultX, 2) + Math.pow(o2.characterModel.getRel_x() - defaultY, 2));

                return Double.compare(product1, product2);
            }
        });

        counter = 0;
        while (this.hasNext(characters)){
            CharacterHandler current = (CharacterHandler) this.getNext(characters);
            if (current.characterModel.getRel_x() == defaultX && current.characterModel.getRel_y() == defaultY){
                return current;
            }
            counter = counter+1;
        }
        return null;
    }

    public void setDefaultX(int defaultX) {
        this.defaultX = defaultX;
    }

    public void setDefaultY(int defaultY) {
        this.defaultY = defaultY;
    }
}
