package AbstractFactory;
import Map.Map;

import Character.Enemies.Enemy;

public interface EnemyFactory {
     Enemy GetEnemy(String enemyType, int x, int y, Map map);
}
