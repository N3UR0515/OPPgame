package AbstractFactory;

import AbstractFactory.Monster.Runner;
import AbstractFactory.Monster.Walker;
import AbstractFactory.Monster.Crawler;
import Character.Enemies.Enemy;
import Map.Map;
import Server.Server;

public class MonsterFactory implements EnemyFactory {
    @Override
    public Enemy GetEnemy(String enemyType, int x, int y, Map map) {
        if(enemyType.equalsIgnoreCase("WALKING")){
            return new Walker(8, map, x, y);
        }else if(enemyType.equalsIgnoreCase("CRAWLING")){
            return new Crawler(3, map, x, y);
        } else {
            return new Runner(10, map, x, y);
        }
    }
}
