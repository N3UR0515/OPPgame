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

                    Packet packet = new Packet(enemyId, enemyModel.getRel_x(), enemyModel.getRel_y(), true);
                    try {
                        Server.broadcastPacket(packet);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                //Server.turnline.Next();


            }
        }

    }
}
