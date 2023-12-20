package Mediator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.TextField;

public class ChatInputField extends ChatComponent {
    private TextField textField;

    public ChatInputField(ChatMediator mediator, GameContainer container, int x, int y) {
        super(mediator);
        this.textField = new TextField(container, container.getDefaultFont(), x, y, 200, 20);
        this.textField.setTextColor(Color.red);
    }

    public String getText() {
        return textField.getText();
    }

    public void clear() {
        textField.setText("");
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        textField.render(container, g);
    }
}

