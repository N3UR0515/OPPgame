import Player.Medic.MobileDoc;
import Player.Medic.Priest;
import Player.Medic.Traumatologist;
import Player.PlayerType;

public class MedicFactory extends AbstractFactory {
    @Override
    PlayerType getPlayerType(String playerType) {
        if (playerType.equalsIgnoreCase("Traumatologist")) {
            return new Traumatologist();
        } else if (playerType.equalsIgnoreCase("Priest")) {
            return new Priest();
        }else if (playerType.equalsIgnoreCase("MobileDoc")) {
            return new MobileDoc();
        }
        return null;
    }
}
