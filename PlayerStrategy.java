import org.newdawn.slick.GameContainer;

public interface PlayerStrategy {
    boolean attack(GameContainer container, int x, int y, int rel_x, int rel_y, Map map, Player player);
}
