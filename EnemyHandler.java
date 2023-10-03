import org.lwjgl.Sys;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class EnemyHandler implements Runnable
{
    public int enemyId;
    public Enemy enemyModel;

    public EnemyHandler(int enemyId) throws IOException {
        this.enemyId = enemyId;
        Random rng = new Random();
        EnemyFactory factory = new EnemyFactory();
        enemyModel = factory.createEnemy(rng.nextInt(15), 0, 4);
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
//                turnline.Remove(enemyModel);
                if(!Server.clients.isEmpty())
                {

                    enemyModel.updateCharacter(Server.clients.get(0).playerModel);
                    Server.broadcastEnemyPositions();

                    turnline.Next();
                }

            }
        }

    }
}
