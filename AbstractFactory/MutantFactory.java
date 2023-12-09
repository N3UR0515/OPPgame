package AbstractFactory;

import AbstractFactory.Monster.Bomber;
import AbstractFactory.Monster.Spitter;
import AbstractFactory.Monster.Witch;
import Character.Enemies.Enemy;
import Server.Server;
import Map.Map;

public class MutantFactory implements EnemyFactory {
    @Override
    public Enemy GetEnemy(String enemyType, int x, int y, Map map) {
        if(enemyType.equalsIgnoreCase("BOMBER")){
            return new Bomber(20, map, x, y);
        }else if(enemyType.equalsIgnoreCase("SPITTER")){
            return new Spitter(16, map, x, y);
        } else {
            return new Witch(100, map, x, y);
        }
    }
}
