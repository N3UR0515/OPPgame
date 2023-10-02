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
        if(!characterTurns.isEmpty())
        {
            characterTurns.removeIf(c -> c.equals(character));
        }

    }
    public void Next()
    {
        if(!characterTurns.isEmpty())
        {
            Character character = characterTurns.remove();
            characterTurns.add(character);
        }

    }
    public Character getCharacter()
    {
        if(!characterTurns.isEmpty())
        {
            return characterTurns.peek();
        }
        return null;
    }
    public void Add(Character character)
    {
        characterTurns.add(character);
    }
    //public int getSize()
    //{
    //    return characterTurns.size();
    //}
}
