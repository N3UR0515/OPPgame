package CharacterDecorator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class MarkedCharacter extends CharacterDecorator{
    public MarkedCharacter(UIElement element) {
        super(element);
    }

    @Override
    public void drawCharacter(Graphics g, int x, int y, int HP) {
        element.drawCharacter(g, x, y, HP);
        markCharacter(g, x, y, HP);
    }

    private void markCharacter(Graphics g, int x, int y, int HP){
        int outerBoxX = 50;
        int outerBoxY = 50;
        int outerBoxWidth = 100;
        int outerBoxHeight = 20;

        g.setColor(Color.white);
        g.fillRect(outerBoxX, outerBoxY, outerBoxWidth, outerBoxHeight);
    }
}
