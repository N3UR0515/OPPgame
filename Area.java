import java.util.ArrayList;
import java.util.List;

public class Area {
    private final List<Character> characters = new ArrayList<>();

    public Area() {}

    public void addCharacter(Character character) {
        if (!characters.contains(character)) {
            characters.add(character);
            character.addArea(this);
        }

    }

    public void removeCharacter(Character character) {
        boolean hasRemoved = characters.remove(character);
        if (hasRemoved) {
            character.removeArea(this);
        }
    }

    public void sendAttack(int x, int y) {
        for (Character c : characters) {
            if (c.getRel_x() == x && c.getRel_y() == y){
                c.damageCharacter();
            }
        }
    }
}
