package Server;


import AbstractFactory.EnemyFactory;
import AbstractFactory.Monster.Walker;
import AbstractFactory.Monster.Witch;
import AbstractFactory.MonsterFactory;
import AbstractFactory.MutantFactory;
import Artifact.MagicStaff;
import Artifact.WarmQuartz;
import Character.Enemies.Enemy;
import Effects.BleedingOutEffect;
import Effects.BurningEffect;
import Effects.IgnitingEffect;
import Map.Area;
import Map.Tile.Tile;
import Packet.Builder.ChangeOfEnemyPositionPacketBuilder;
import Packet.Builder.DamagePlayerPacketBuilder;
import Packet.Builder.PacketBuilder;
import Packet.Packet;
import Map.Tile.FieryTile;
import Map.Tile.UnavailableTile;
import Packet.*;
import Character.Player;
import Character.Character;
import org.lwjgl.Sys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EnemyCompositeHandler extends CharacterHandler
{
    protected EnemyFactory factory;
    protected ArrayList<CharacterHandler> children;
    protected EnemyCompositeHandler parent;
    public EnemyCompositeHandler(int enemyId) throws IOException {
        this.characterId = enemyId;
        Random rng = new Random();
        children = new ArrayList<>();

        factory = new MutantFactory();
       /* String enemyType = "";
        int randomIndex = rng.nextInt(3);

        if (rng.nextBoolean())
        {
            factory = new MutantFactory();
            enemyType =  (randomIndex == 0) ? "SPITTER" : (randomIndex == 1) ? "BOMBER" : "OTHER_TYPE";;
        } else {
            factory = new MonsterFactory();
            enemyType = (randomIndex == 0) ? "CRAWLING" : (randomIndex == 1) ?  "WALKING" : "OTHER_TYPE";
        }*/

        int x, y;
        do {
            x = 3;//rng.nextInt(100);
            y = 3;//rng.nextInt(100);
        } while (Server.map.getTileByLoc(x, y).getClass() == UnavailableTile.class);

        //characterModel = factory.GetEnemy(enemyType, x, y);
        characterModel = factory.GetEnemy("WITCH", x, y, Server.map);

        assignArtifact();

        characterModel.id = enemyId;
        characterModel.setEffects(new BleedingOutEffect());
        Turnline.getInstance().Add(characterModel);
    }

    public void add(CharacterHandler characterHandler)
    {
        children.add(characterHandler);
    }
    public void remove(CharacterHandler characterHandler)
    {
        children.remove(characterHandler);
    }
    public ArrayList<CharacterHandler> getChildren()
    {
        return children;
    }

    @Override
    public void sendPacket(Packet packet) throws IOException {}

    @Override
    protected void move() {
        if(!Server.clients.isEmpty())
        {
            Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y()).setOnTile(null);
            characterModel.updateCharacter(Server.clients.get(1).characterModel);
            Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y()).setOnTile(characterModel);

            PacketBuilder builder;
            builder = new ChangeOfEnemyPositionPacketBuilder();
            PacketDirector.constructChangeOfEnemyPositionPacket(builder, (Enemy) characterModel);

            Packet packet = builder.getPacket();
            packet.setEnemyType("WITCH");
            try {
                Server.broadcastPacket(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Iterator<CharacterHandler> iterator = children.iterator();
            while(iterator.hasNext()) {
                CharacterHandler child = iterator.next();
                child.run();
                if(child.characterModel.getHP() <= 0)
                {
                    iterator.remove();
                }
            }
            if(children.isEmpty())
            {
                try {
                    EnemyHandler enemy =new EnemyHandler(characterId * 100);
                    enemy.setParent(this);
                    children.add(enemy);
                    if(characterId == 0)
                    {
                        EnemyCompositeHandler enemyComposite = new EnemyCompositeHandler(characterId * 100 + 1);
                        enemyComposite.parent = this;
                        children.add(enemyComposite);
                    }
                    //
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    @Override
    protected void attack() {
        if(!Server.clients.isEmpty()) {
            PacketBuilder builder;
            builder = new DamagePlayerPacketBuilder();
            PacketDirector.constructDamagePlayerPacket(builder, (Player) Server.clients.get(1).characterModel);
            Packet p = builder.getPacket();
            p.setEnemyType("WITCH");
            try {
                if (!Server.clients.isEmpty())
                    Server.clients.get(1).sendPacket(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (Server.clients.get(1).characterModel.getHP() <= 0)
                Turnline.getInstance().Remove(Server.clients.get(1).characterModel);

        }
    }

    @Override
    protected void assignArtifact() {
        Random rng = new Random();
        //Randomly decide whether to assign artifact, and if yes - which one
        int artifactRoll = rng.nextInt(8);
        if (artifactRoll == 2 || artifactRoll == 3) {
            characterModel.setArtifact(new WarmQuartz());
        } else if (artifactRoll == 1) {
            characterModel.setArtifact(new MagicStaff());
        }
    }

    @Override
    protected boolean checkForTurn() {
        Turnline turnline = Turnline.getInstance();
        if(parent != null)
            return turnline.getCharacter() != null && turnline.getCharacter() instanceof Enemy && turnline.getCharacter().id == parent.characterId;
        return turnline.getCharacter() != null && turnline.getCharacter() instanceof Enemy && turnline.getCharacter().id == characterId;
    }

    @Override
    protected void receiveTileDamage() {
        characterModel.damageCharacter();
        if(parent != null)
            parent.characterModel.damageCharacter();
    }

    @Override
    protected void usePickUp() {
        Tile tile = Server.map.getTileByLoc(characterModel.getRel_x(), characterModel.getRel_y());
        if(tile.getPickUp() != null)
            characterModel.UseAndDeleteEffect(tile.getPickUp().getPickupCode());
    }

    @Override
    protected boolean checkForAttack() {
        return true;
    }

    @Override
    protected boolean checkForMove() {
        return true;
    }

}
