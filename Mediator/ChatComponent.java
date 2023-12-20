package Mediator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public abstract class ChatComponent {
    protected ChatMediator mediator;

    public ChatComponent(ChatMediator mediator) {
        this.mediator = mediator;
    }

    public abstract void render(GameContainer container, Graphics g);
}
