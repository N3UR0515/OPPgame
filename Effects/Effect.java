package Effects;

import Character.Character;

public abstract class Effect {
    private Effect next;

    public static Effect link(Effect first, Effect... chain)
    {
        Effect head = first;
        for (Effect nextInChain : chain)
        {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract void affect(Character character);

    protected void affectNext(Character character)
    {
        if(next == null)
            return;
        next.affect(character);
    }
}
