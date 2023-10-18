package AbstractFactory;

import Zombie.WalkingZombie;
import Zombie.CrawlingZombie;
import Zombie.Zombie;
public class ZombieFactory extends AbstractFactory{
    @Override
    public Zombie getZombie(String ZombieType) {
        if(ZombieType.equalsIgnoreCase("WALKING")){
            return new WalkingZombie();
        }else if(ZombieType.equalsIgnoreCase("CRAWLING")){
            return new CrawlingZombie();
        }
        return null;
    }
}
