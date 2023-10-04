import Player.Soldier.Infantry;
import Player.Soldier.MachineGunner;
import Player.Soldier.TankDriver;
import Player.PlayerType;

public class SoldierFactory extends AbstractFactory {
    @Override
    public PlayerType getPlayerType(String playerType) {
        if (playerType.equalsIgnoreCase("TankDriver")) {
            return new TankDriver();
        } else if (playerType.equalsIgnoreCase("MachineGunner")) {
            return new MachineGunner();
        }else if (playerType.equalsIgnoreCase("Infantry")) {
            return new Infantry();
        }
        return null;
    }
}
