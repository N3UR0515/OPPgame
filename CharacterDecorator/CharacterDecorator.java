package CharacterDecorator;

import org.newdawn.slick.Graphics;

public abstract class CharacterDecorator implements UIElement {
    protected UIElement element;

    public CharacterDecorator(UIElement element){
        this.element = element;
    }
    public void drawCharacter(Graphics g, int x, int y, int HP){
    }
}
