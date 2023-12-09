package FlyWeight;

import FlyWeight.MonsterImage;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SharedMonsterImage implements MonsterImage {
    private Image sharedImage;

    public SharedMonsterImage(Image image){
        sharedImage = image;
    }

    @Override
    public void draw(Graphics g, int x, int y, int HP) {
        float imageX = x - sharedImage.getWidth() / 2f;
        float imageY = y - sharedImage.getHeight() / 2f;
        sharedImage.draw(imageX, imageY);
    }
}
