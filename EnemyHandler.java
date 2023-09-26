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

        //this.run();
    }
    @Override
    public void run() {
        //while (true)
        {

            //System.out.println("aaaaaaaa");
           /* try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/

            if ( Server.turnline.getCharacter() != null && Server.turnline.getCharacter() instanceof Enemy && Server.turnline.getCharacter().id == enemyId)
            {
                Server.turnline.Remove(enemyModel);
                if(Server.turnline.getCharacter() != null && Server.turnline.getCharacter() instanceof Player)
                {
                    enemyModel.updateCharacter((Player)Server.turnline.getCharacter());
                }

                Server.broadcastEnemyPositions();

                //Server.turnline.Next();


            }
        }

    }
}
