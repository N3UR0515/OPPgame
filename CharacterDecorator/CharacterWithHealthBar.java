package CharacterDecorator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class CharacterWithHealthBar extends CharacterDecorator{
    public CharacterWithHealthBar(UIElement element) {
        super(element);
    }

    @Override
    public void drawCharacter(Graphics g, int x, int y, int HP) {
        element.drawCharacter(g, x, y, HP);
        addHealthBar(g, x, y, HP);
    }

    private void addHealthBar(Graphics g, int x, int y, int HP){
        int outerBoxX = 50;
        int outerBoxY = 50;
        int outerBoxWidth = 100;
        int outerBoxHeight = 20;

        int innerBoxWidth = (int) ((double) HP / 10 * outerBoxWidth);

        g.setColor(Color.red);
        g.fillRect(outerBoxX, outerBoxY, outerBoxWidth, outerBoxHeight);

        g.setColor(Color.green);
        g.fillRect(outerBoxX, outerBoxY, innerBoxWidth, outerBoxHeight);
    }
}
