import java.util.LinkedList;
import java.util.Queue;

public class Turnline {
    private Queue<Character> characterTurns;

    public Turnline()
    {
        characterTurns = new LinkedList<>();
    }
    public void Remove(Character character)
    {
        characterTurns.removeIf(c -> c.equals(character));
    }
    public void Next()
    {
        Character character = characterTurns.remove();
        characterTurns.add(character);
    }
    public Character getCharacter()
    {
        return characterTurns.peek();
    }
    public void Add(Character character)
    {
        characterTurns.add(character);
    }
}
