import Player.PlayerType;
import Player.Scout.CamoExpert;
import Player.Scout.Recon;
import Player.Scout.Thief;

public class ScoutFactory extends AbstractFactory {
    @Override
    PlayerType getPlayerType(String playerType) {
        if (playerType.equalsIgnoreCase("CamoExpert")) {
            return new CamoExpert();
        } else if (playerType.equalsIgnoreCase("Thief")) {
            return new Thief();
        }else if (playerType.equalsIgnoreCase("Recon")) {
            return new Recon();
        }
        return null;
    }
}
