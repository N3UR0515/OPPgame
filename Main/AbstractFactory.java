package Main;

import Zombie.Zombie;

public abstract class AbstractFactory {
    abstract Zombie getZombie(String playerType);
}
