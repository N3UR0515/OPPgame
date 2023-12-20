package Mediator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.Graphics;

public class SendMessageButton extends ChatComponent {
    private MouseOverArea buttonArea;
    private Image buttonImage;
    private String buttonText;
    private int x, y;

    public SendMessageButton(ChatMediator mediator, GameContainer container, Image buttonImage, String buttonText, int x, int y) {
        super(mediator);
        this.buttonImage = buttonImage;
        this.buttonText = buttonText;
        this.x = x;
        this.y = y;
        this.buttonArea = new MouseOverArea(container, buttonImage, x, y, buttonImage.getWidth(), buttonImage.getHeight());
        System.out.println("Button Area: " + x + ", " + y + ", " + buttonImage.getWidth() + ", " + buttonImage.getHeight());
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        buttonArea.render(container, g);
        g.drawString(buttonText, x + (buttonImage.getWidth() - g.getFont().getWidth(buttonText)) / 2,
                y + (buttonImage.getHeight() - g.getFont().getHeight(buttonText)) / 2);
    }

    public void onClick() {
        mediator.notify(this, "click");
    }

    public void checkClick(Input input) {
        if (buttonArea.isMouseOver() && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            onClick();
        }
    }
}

