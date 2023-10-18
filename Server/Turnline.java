package Server;

import java.util.LinkedList;
import java.util.Queue;
import Character.Character;

public class Turnline {
    private Queue<Character> characterTurns;
    private static Turnline instance;

    private Turnline(){characterTurns = new LinkedList<>();}
    public synchronized static Turnline getInstance()
    {
        if(instance == null)
        {
            instance = new Turnline();
        }
        return  instance;
    }
    public synchronized void Remove(Character character)
    {
        synchronized (this)
        {
            if(!characterTurns.isEmpty())
            {
                characterTurns.removeIf(c -> c.equals(character));
            }
        }

    }
    public synchronized void Next()
    {
        synchronized (this)
        {
            if(!characterTurns.isEmpty())
            {
                Character character = characterTurns.remove();
                characterTurns.add(character);
            }
        }


    }
    public Character getCharacter()
    {
        synchronized (this)
        {
            if(!characterTurns.isEmpty())
            {
                return characterTurns.peek();
            }
            return null;
        }

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
