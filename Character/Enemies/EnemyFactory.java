package Character.Enemies;

import Character.Enemies.*;
import Server.Server;

public class EnemyFactory {
    public EnemyFactory() {}
    public Enemy createEnemy(int key, int x, int y){
        if (key < 3) {
            return new ZombieOld(8, Server.map, x, y);
        } else if (key < 6) {
            return new Werewolf(6, Server.map, x, y);
        } else if (key < 9) {
            return new Bowman(7, Server.map, x, y);
        } else if (key == 9) {
            return new InfectiousZombie(8, Server.map, x, y);
        } else if (key == 10) {
            return new VolatileZombie(8, Server.map, x, y);
        } else if (key == 11) {
            return new UnstableWerewolf(6, Server.map, x, y);
        } else if (key == 12) {
            return new ResilientWerewolf(6, Server.map, x, y);
        } else if (key == 13) {
            return new Crossbowman(7, Server.map, x, y);
        } else {
            return new Conscript(7, Server.map, x, y);
        }
    }
}
