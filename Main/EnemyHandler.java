package Main;

import Tile.FieryTile;

import java.io.IOException;
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
        enemyModel.id = enemyId;
        Turnline.getInstance().Add(enemyModel);
    }
    @Override
    public void run() {
        {
            Turnline turnline = Turnline.getInstance();
            if ( turnline.getCharacter() != null && turnline.getCharacter() instanceof Enemy && turnline.getCharacter().id == enemyId)
            {
                turnline.Remove(enemyModel);
                PacketBuilder builder;
                if(turnline.getCharacter() != null && turnline.getCharacter() instanceof Player)
                {
                    enemyModel.updateCharacter((Player)turnline.getCharacter());

                    builder = new DamagePlayerPacketBuilder();
                    PacketDirector.constructDamagePlayerPacket(builder, (Player)turnline.getCharacter());
                    Packet p = builder.getPacket();
                    /*Packet p = new Packet(-1, -1, -1, true);
                    p.setAttack(true);
                    p.setHP(turnline.getCharacter().getHP());*/
                    try {
                        Server.clients.get(0).sendPacket(p);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if(turnline.getCharacter().getHP() <= 0)
                        turnline.Remove(turnline.getCharacter());

                    builder = new ChangeOfEnemyPositionPacketBuilder();
                    PacketDirector.constructChangeOfEnemyPositionPacket(builder, enemyModel);

                    Packet packet = builder.getPacket();
                    try {
                        Server.broadcastPacket(packet);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (Server.map.getTileByLoc(enemyModel.getRel_x(), enemyModel.getRel_y()).getClass() == FieryTile.class) {
                    enemyModel.damageCharacter();
                }
            }
        }

    }
}
