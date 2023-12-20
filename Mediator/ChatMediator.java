package Mediator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMediator implements Mediator {
    private ChatInputField chatInputField;
    private SendMessageButton sendMessageButton;
    private ChatHistory chatHistory;

    public ChatMediator(GameContainer container, Image buttonImage) {
        this.chatHistory = new ChatHistory(this, 10, 100);
        this.chatInputField = new ChatInputField(this, container, 10, 500);
        this.sendMessageButton = new SendMessageButton(this, container, buttonImage, "Send", 220, 500);
    }

    @Override
    public void notify(ChatComponent component, String event) {
        if (component instanceof SendMessageButton && event.equals("click")){
            String message = chatInputField.getText();
            if (!message.isEmpty()) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                chatHistory.addMessage("[" + dtf.format(now) + "]: " + message);
                chatInputField.clear();
            }
        }

    }

    public ChatHistory getChatHistory() {
        return chatHistory;
    }

    public ChatInputField getChatInputField() {
        return chatInputField;
    }

    public SendMessageButton getSendMessageButton() {
        return sendMessageButton;
    }
}
