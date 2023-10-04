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
        enemyModel = factory.createEnemy(rng.nextInt(15), 0, 5);
        Turnline.getInstance().Add(enemyModel);
    }
    @Override
    public void run() {
        {
            Turnline turnline = Turnline.getInstance();
            if ( turnline.getCharacter() != null && turnline.getCharacter() instanceof Enemy && turnline.getCharacter().id == enemyId)
            {
                turnline.Remove(enemyModel);
                if(turnline.getCharacter() != null && turnline.getCharacter() instanceof Player)
                {
                    enemyModel.updateCharacter((Player)turnline.getCharacter());
                    Packet p = new Packet(-1, -1, -1, true);
                    p.isAttack = true;
                    p.HP = turnline.getCharacter().getHP();
                    try {
                        Server.clients.get(0).sendPacket(p);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if(turnline.getCharacter().getHP() <= 0)
                        turnline.Remove(turnline.getCharacter());

                    Packet packet = new Packet(enemyId, enemyModel.getRel_x(), enemyModel.getRel_y(), true);
                    try {
                        Server.broadcastPacket(packet);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                //turnline.Next();


            }
        }

    }
}
