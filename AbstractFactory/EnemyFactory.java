package AbstractFactory;

import Character.Enemies.Enemy;

public interface EnemyFactory {
     Enemy GetEnemy(String enemyType, int x, int y);
}
