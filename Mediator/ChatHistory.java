package Mediator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

public class ChatHistory extends ChatComponent {
    private List<String> messages = new ArrayList<>();
    private int x, y;

    public ChatHistory(ChatMediator mediator, int x, int y) {
        super(mediator);
        this.x = x;
        this.y = y;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        int backgroundHeight = messages.size() * 20;
        g.setColor(new Color(0, 0, 0, 0.8f));
        g.fillRect(x, y, 500, backgroundHeight);

        g.setColor(Color.red);
        int yPos = y;
        for (String message : messages) {
            g.drawString(message, x, yPos);
            yPos += 20;
        }
        g.setColor(Color.white);
    }
}

