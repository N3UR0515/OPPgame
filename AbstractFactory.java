import Player.PlayerType;

public abstract class AbstractFactory {
    abstract PlayerType getPlayerType(String playerType);
}
