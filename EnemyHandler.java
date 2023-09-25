import org.lwjgl.Sys;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class EnemyHandler implements Runnable
{
    public int enemyId;
    public Enemy enemyModel;

    public EnemyHandler(int enemyId) throws IOException {
        this.enemyId = enemyId;
        enemyModel = new Enemy(10, Server.map, 0, 0);
        Server.turnline.Add(enemyModel);

        new Thread(this::run).start();
    }
    @Override
    public void run() {
        if( Server.turnline.getCharacter().id == enemyId)
        {
           // System.out.println(Server.turnline.getCharacter().id);
            enemyModel.updateCharacter(new Player(10, Server.map, 1, 1));
            Server.broadcastEnemyPositions();
            Server.turnline.Next();
        }
    }
}
