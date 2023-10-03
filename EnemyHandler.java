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
        Turnline.getInstance().Add(enemyModel);

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
            Turnline turnline = Turnline.getInstance();
            if ( turnline.getCharacter() != null && turnline.getCharacter() instanceof Enemy && turnline.getCharacter().id == enemyId)
            {
                turnline.Remove(enemyModel);
                if(turnline.getCharacter() != null && turnline.getCharacter() instanceof Player)
                {
                    enemyModel.updateCharacter((Player)turnline.getCharacter());
                }

                Server.broadcastEnemyPositions();

                //Server.turnline.Next();


            }
        }

    }
}
