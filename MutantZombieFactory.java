import Zombie.TankZombie;
import Zombie.SpitterZombie;
import Zombie.Zombie;
public class MutantZombieFactory extends AbstractFactory{
    @Override
    public Zombie getZombie(String ZombieType) {
        if(ZombieType.equalsIgnoreCase("TANK")){
            return new TankZombie();
        }else if(ZombieType.equalsIgnoreCase("SPITTER")){
            return new SpitterZombie();
        }
        return null;
    }
}
