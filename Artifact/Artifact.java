package Artifact;

import java.util.Random;
import Character.Character;

public abstract class Artifact {
    protected Random rng;

    public Artifact() {
        rng = new Random();
    }

    public abstract void rollForEffect(Character character, int HPCap);
}
