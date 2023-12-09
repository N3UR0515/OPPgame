package FlyWeight;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.awt.*;
import java.util.HashMap;

public class FlyWeightFactory {
    private static final HashMap<String, MonsterImage> monsterImageCache = new HashMap<>();

    public static MonsterImage getMonsterImage(String imagePath) throws SlickException {
            if (!monsterImageCache.containsKey(imagePath)) {

                Image image = new Image(imagePath);
                Image resizedImage = image.getScaledCopy(20, 20);
                monsterImageCache.put(imagePath, new SharedMonsterImage(resizedImage));
            }


            return monsterImageCache.get(imagePath);
    }
}
