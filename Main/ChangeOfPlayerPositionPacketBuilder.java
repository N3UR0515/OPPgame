package Main;

public class ChangeOfPlayerPositionPacketBuilder extends PacketBuilder{
    @Override
    public Packet getPacket() {
        return new Packet().setId(id).setX(x).setY(y).setAttack(false).setEnemy(false);
    }
}
